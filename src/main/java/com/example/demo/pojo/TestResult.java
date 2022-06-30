package com.example.demo.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestResult{

	@JsonProperty("content")
	private List<ContentItem> content;

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ContentItem{

		@JsonProperty("manual")
		private boolean manual;

		@JsonProperty("known")
		private boolean known;

		@JsonProperty("muted")
		private boolean muted;

		@JsonProperty("historyKey")
		private String historyKey;

		@JsonProperty("fullName")
		private String fullName;

		@JsonProperty("message")
		private String message;

//		@JsonProperty("tags")
//		private List<TagsItem> tags;

		@JsonProperty("launchId")
		private int launchId;

		@JsonProperty("projectId")
		private int projectId;

		@JsonProperty("testCaseId")
		private int testCaseId;

		@JsonProperty("status")
		private String status;
	}
}