package com.bmn.sns;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Random;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseClientBuilder;
import com.amazonaws.services.kinesisfirehose.model.PutRecordRequest;
import com.amazonaws.services.kinesisfirehose.model.PutRecordResult;
import com.amazonaws.services.kinesisfirehose.model.Record;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.bmn.sns.model.Records;
import com.bmn.sns.model.S3;
import com.bmn.sns.model.SnsResult;
import com.bmn.sns.model.SnsSubscriptionConfirmationModel;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController("/sns/api")
public class SnsController {
  
    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    static DynamoDB dynamoDB = new DynamoDB(client);

    static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    static String productCatalogTableName = "ProductCatalog";
    


	
	@PostMapping(consumes= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_JSON_UTF8_VALUE,MediaType.APPLICATION_XML_VALUE,MediaType.TEXT_PLAIN_VALUE})
	public void postSNS(@RequestHeader(value = "x-amz-sns-message-type") String snsMessageType,
			@RequestBody String snsResponse) throws JsonParseException, JsonMappingException, IOException {
		System.out.println("Inside the Post SNS method ");
		ObjectMapper mapper = new ObjectMapper();
		SnsSubscriptionConfirmationModel subscriptionResult = mapper.readValue(snsResponse,
				SnsSubscriptionConfirmationModel.class);
		if (snsMessageType.equals("SubscriptionConfirmation")) {
			System.out.println("Confirming subscription for the arn " + subscriptionResult.getTopicArn());
			Scanner sc = new Scanner(new URL(subscriptionResult.getSubscribeURL()).openStream());
			StringBuilder sb = new StringBuilder();
			while (sc.hasNextLine()) {
				sb.append(sc.nextLine());
			}
			System.out.println("Subscription confirmation (" + subscriptionResult.getSubscribeURL() + ") Return value: "
					+ sb.toString());
		} else if ("Notification".equals(snsMessageType)) {
			System.out.println(" notification response .." + snsResponse);
			String s3ObjectName = getS3ObjectName(snsResponse, mapper);
			try {
				getFileFromS3(s3ObjectName);
				createDynamoDBTables();
				firehouseStreamToS3();
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("Received notification..");
		} else {
			System.out.println("Nothing can be done..");
		}

	}

    /**
     * Reads the S3 object key name (filename) from the SNS Notification message 
     *
     * @param snsResponse , mapper
     *           SNS response string and the object mapper 
     *
     * @throws IOException
     */
	private String getS3ObjectName(String snsResponse, ObjectMapper mapper )
			throws IOException, JsonParseException, JsonMappingException {
		System.out.println("Inside the Notification - get S3 ObjectName - method");
		SnsSubscriptionConfirmationModel notificationResult = mapper.readValue(snsResponse, SnsSubscriptionConfirmationModel.class);

		String s3ObjFileName ="";
		System.out.println("Inside the Notification - method" + notificationResult.getMessage());
		SnsResult SnsResult = mapper.readValue(notificationResult.getMessage(), SnsResult.class);
		System.out.println("Subscription Notification - SNS Result (" + SnsResult.getRecords()[0]);
		Records records = SnsResult.getRecords()[0];
		if (records != null && records.getS3() != null) {
			S3 s3 = records.getS3();
			s3ObjFileName = s3.getObject().getKey();
			String logMsgAndSubject = " File Name from S3 : " + s3.getObject().getKey();
			System.out.println(" Notification (" + logMsgAndSubject);
		}
		return s3ObjFileName;
	}
	
    /**
     * Reads the file from the S3 bucket and convert the text file to csv as output.csv
     *
     * @param s3ObjectName
     *            The S3 file name from the SNS 
     *
     * @throws IOException
     */
    
    static void getFileFromS3(String s3ObjectName) throws IOException {
    	
    	AWSCredentials credentials = null;
        try {
        	// Load credentials from a local profile / loggedinuser /.aws
           // credentials = new ProfileCredentialsProvider().getCredentials();
        	
        	// This is load the credentials via the aws key and secret 
        	credentials = new BasicAWSCredentials("AKIAJASBUOVVL5UCQ35Q", "iX6nQORSSD/4iNwH4LyE2TOeaw2mGrJIFfLopKTz"); 	
        	
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (~/.aws/credentials), and is in valid format.",
                    e);
        }

        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.US_EAST_1)
            .build();

        String bucketName = "from-bala";
        String key = s3ObjectName;
        
        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon S3");
        System.out.println("===========================================\n");

        try {
        
            /*
             * List the buckets in your account
             */
            System.out.println("Listing buckets");
            for (Bucket bucket : s3.listBuckets()) {
                System.out.println(" - " + bucket.getName());
            }
            System.out.println();
            
            System.out.println("Downloading an object");
            S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
            System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
            generateOutputFileFromStream(object.getObjectContent());

    } catch (AmazonServiceException ase) {
        System.out.println("Caught an AmazonServiceException, which means your request made it "
                + "to Amazon S3, but was rejected with an error response for some reason.");
        System.out.println("Error Message:    " + ase.getMessage());
        System.out.println("HTTP Status Code: " + ase.getStatusCode());
        System.out.println("AWS Error Code:   " + ase.getErrorCode());
        System.out.println("Error Type:       " + ase.getErrorType());
        System.out.println("Request ID:       " + ase.getRequestId());
    } catch (AmazonClientException ace) {
        System.out.println("Caught an AmazonClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with S3, "
                + "such as not being able to access the network.");
        System.out.println("Error Message: " + ace.getMessage());
    }
}
    /**
     * Displays the contents of the specified input stream as text.
     *
     * @param input
     *            The input stream from the s3 object file 
     *
     * @throws IOException
     */
	private static void generateOutputFileFromStream(InputStream input) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
		BufferedWriter bufferedWriter = null;
		java.util.List<String> list = new ArrayList<String>();
		try {

			bufferedWriter = new BufferedWriter(new FileWriter("output.csv"));
			String str = "";
			while ((str = bufferedReader.readLine()) != null) {
				System.out.println("inside the while loop");
				if(str.contains(":")) {
					int indexOf = str.indexOf(':');
					bufferedWriter.write(str.substring(indexOf + 2));
					bufferedWriter.write(",");
					bufferedWriter.flush();		
				}

			}
			System.out.println("Done!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// close without throwing exception
			bufferedReader.close();
			bufferedWriter.close();
			// IOUtils.closeQuietly(bufferedReader);
			// IOUtils.closeQuietly(bufferedWriter);
		}
	}
        
        
    /**
     * DynamoDb table creation and as well as insert records from the csv files 
     *
     *
     * @throws IOException
     */

	public void createDynamoDBTables() throws IOException {
		String[] readFileFromCSV = readFileFromCSV();
		

		// https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/AppendixSampleDataCodeJava.html
		final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
		final DynamoDB dynamoDB = new DynamoDB(client);

		final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

		final String customerInvoice = "CustomerInvoice";


		try {
			//deleteTable(customerInvoice);
			createTable(customerInvoice, 10L, 5L, "Id", "N");
			createItems(customerInvoice,readFileFromCSV);

		} catch (Exception e) {
			System.err.println("Program failed:");
			System.err.println(e.getMessage());
		}
		System.out.println("Success.");
	}
	
			
    /**
     * Creates a dynamodb table with partitionkeyname and keyType 
     *
     *
     * @throws IOException
     */
	
    private static void createTable(String tableName, long readCapacityUnits, long writeCapacityUnits,
            String partitionKeyName, String partitionKeyType) {

            createTable(tableName, readCapacityUnits, writeCapacityUnits, partitionKeyName, partitionKeyType, null, null);
        }

	private static void createTable(String tableName, long readCapacityUnits, long writeCapacityUnits,
			String partitionKeyName, String partitionKeyType, String sortKeyName, String sortKeyType) {

		try {

			ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
			keySchema.add(new KeySchemaElement().withAttributeName(partitionKeyName).withKeyType(KeyType.HASH)); // Partition
																													// key

			ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
			attributeDefinitions.add(
					new AttributeDefinition().withAttributeName(partitionKeyName).withAttributeType(partitionKeyType));

			if (sortKeyName != null) {
				keySchema.add(new KeySchemaElement().withAttributeName(sortKeyName).withKeyType(KeyType.RANGE)); // Sort
																													// key
				attributeDefinitions
						.add(new AttributeDefinition().withAttributeName(sortKeyName).withAttributeType(sortKeyType));
			}

			CreateTableRequest request = new CreateTableRequest().withTableName(tableName).withKeySchema(keySchema)
					.withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(readCapacityUnits)
							.withWriteCapacityUnits(writeCapacityUnits));

			String replyTableName = "Reply";
			// If this is the Reply table, define a local secondary index
			if (replyTableName.equals(tableName)) {

				attributeDefinitions
						.add(new AttributeDefinition().withAttributeName("PostedBy").withAttributeType("S"));

				ArrayList<LocalSecondaryIndex> localSecondaryIndexes = new ArrayList<LocalSecondaryIndex>();
				localSecondaryIndexes.add(new LocalSecondaryIndex().withIndexName("PostedBy-Index")
						.withKeySchema(
								new KeySchemaElement().withAttributeName(partitionKeyName).withKeyType(KeyType.HASH), // Partition
																														// key
								new KeySchemaElement().withAttributeName("PostedBy").withKeyType(KeyType.RANGE)) // Sort
																													// key
						.withProjection(new Projection().withProjectionType(ProjectionType.KEYS_ONLY)));

				request.setLocalSecondaryIndexes(localSecondaryIndexes);
			}

			request.setAttributeDefinitions(attributeDefinitions);

			Table table = dynamoDB.getTable(tableName);
			if (table != null) {
				dynamoDB.createTable(request);

			}

			System.out.println("Waiting for " + tableName + " to be created...this may take a while...");
			table.waitForActive();

		} catch (Exception e) {
			System.err.println("CreateTable request failed for " + tableName);
			System.err.println(e.getMessage());
		}
	}
    
	
    /**
     * Creates Items and insertion of records to the tables 
     *
     *
     * 
     */
	
    private void createItems(String tableName,String[] invoice) {
	      Table table = dynamoDB.getTable(tableName);
	       final int PRODUCT_ID;
	      PRODUCT_ID = new Random().nextInt(1000);
	      try {  
	         Item item = new Item() 
	            .withPrimaryKey("Id", PRODUCT_ID)
	            .withString("CustomerID", invoice[0]) 
	            .withString("InvID", invoice[1]) 
	            .withString("Date", invoice[2]) 
	            .withString("From", invoice[3]) 
	            .withString("To", invoice[4]) 
	            .withNumber("Amount", new BigDecimal(invoice[5])) 
	            .withNumber("SGST", new BigDecimal(invoice[6]) )
	            .withNumber("Total", new BigDecimal(invoice[7]))
	            .withString("InWords", invoice[8]) ;
       
	         table.putItem(item);  
	      } catch (Exception e) { 
	         System.err.println("Cannot create items."); 
	         System.err.println(e.getMessage()); 
	      } 
	   }   
    
    /**
     * Read the data from csv and pushes the stream to the firehose 
     *
     *
     * 
     */
	 public void firehouseStreamToS3() {
	    	String data = convertStringArrayToString(readFileFromCSV(),",");
	        System.out.println("Inside the firehoseStream " + data);
	    
	    	//https://cloudacademy.com/blog/everything-you-ever-wanted-to-know-about-amazon-kinesis-firehose/
	    	AWSCredentials credentials = null;
	       	credentials = new BasicAWSCredentials("AKIAJASBUOVVL5UCQ35Q", "iX6nQORSSD/4iNwH4LyE2TOeaw2mGrJIFfLopKTz");
	       	AmazonKinesisFirehose client = AmazonKinesisFirehoseClientBuilder.standard().withCredentials( new AWSStaticCredentialsProvider(credentials)).build();

	    	Record record = new Record();
	    	 
	    	record.setData(ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8)));
	    	 
	    	PutRecordRequest putRecordRequest = new PutRecordRequest()
	    	 
	    	.withDeliveryStreamName("bala-stream")
	    	 
	    	.withRecord(record);
	    	 
	    	putRecordRequest.setRecord(record);
	    	 
	    	PutRecordResult result = client.putRecord(putRecordRequest);
	    	 
	    	System.out.println("Result Inserted with ID: "+result.getRecordId());
	    }
    
    public String[] readFileFromCSV() {
    	
    	String csvFile = "/home/ec2-user/output.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        String[] invoice = null;
        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                invoice = line.split(cvsSplitBy);
                System.out.println("Invoice Date= " + invoice[3] + " , From=" + invoice[4] + "]");

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		return invoice;

    }
    
	private static void deleteTable(String tableName) {
        Table table = dynamoDB.getTable(tableName);
        try {
            System.out.println("Issuing DeleteTable request for " + tableName);
            table.delete();
            System.out.println("Waiting for " + tableName + " to be deleted...this may take a while...");
            table.waitForDelete();

        }
        catch (Exception e) {
            System.err.println("DeleteTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }
    }
    
    private static String convertStringArrayToString(String[] strArr, String delimiter) {
		StringBuilder sb = new StringBuilder();
		for (String str : strArr)
			sb.append(str).append(delimiter);
		return sb.substring(0, sb.length() - 1);
	}
    
/*	@GetMapping
	public void getSns() {
		System.out.println("Hello");
		   //subscribeTopic();
		
	}*/
    
    }
    

