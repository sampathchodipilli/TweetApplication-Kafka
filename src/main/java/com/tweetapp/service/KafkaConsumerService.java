package com.tweetapp.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.tweetapp.constants.Constants;
import com.tweetapp.model.Response;
import com.tweetapp.repository.TweetRepository;

@Service
public class KafkaConsumerService {
	
	@Value("${topic.name}")
	private String topicName;
	
	@Autowired
	private TweetRepository tweetRepository;
	
	private Logger logger = LogManager.getLogger(KafkaConsumerService.class);
	
	@KafkaListener(topics = "tweet-topic", groupId = "tweet-group" )
	public void consumeDeletionMessage(String id) {
		logger.info("id read from kafka topic by consumer = "+id);
		if(id!=null) {
			logger.info("Inside deleteTweet() ... ");
			Response response;
			try {
				tweetRepository.deleteById(id);
				logger.info("Tweet Deleted Successfully !");
				response = new Response(Constants.SUCCESS, Constants.HTTP_OK, "Tweet Deleted");
			} catch (Exception e) {
				logger.error("Error : ",e);
				response = new Response(Constants.FAILED, Constants.BAD_REQUEST, "Tweet Deletion Failed");
			}
		}
	}
}
