package com.brogrammers.agora.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import android.test.ActivityInstrumentationTestCase2;

import com.brogrammers.agora.data.CacheDataManager;
import com.brogrammers.agora.data.DeviceUser;
import com.brogrammers.agora.data.ESDataManager;
import com.brogrammers.agora.data.QuestionController;
import com.brogrammers.agora.helper.QuestionSorter;
import com.brogrammers.agora.model.Question;
import com.brogrammers.agora.views.MainActivity;

import junit.framework.TestCase;


public class SortByVoteTest extends ActivityInstrumentationTestCase2<MainActivity> {

	public SortByVoteTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		HttpClient client = new DefaultHttpClient();
		try {
			HttpDelete deleteRequest = new HttpDelete("http://cmput301.softwareprocess.es:8080/cmput301f14t02/sorttest/_query?q=_type:sorttest");
			client.execute(deleteRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	private class TestDeviceUser extends DeviceUser {
		public TestDeviceUser() {
			setUsername("TestBingsF");
			favoritesPrefFileName = "TEST_FAVORITES";
			cachedPrefFileName = "TEST_CACHED";
			authoredPrefFileName = "TEST_AUTHORED";
			usernamePrefFileName = "TEST_USERNAME";
		}
	}
	
	private class TestCacheManager extends CacheDataManager {
		public TestCacheManager() {
			super("TEST_CACHE");
		}
	}
	
	private class TestESManager extends ESDataManager {
		public TestESManager() {
			super("http://cmput301.softwareprocess.es:8080/", "cmput301f14t02/", "sorttest/");
		}
	}
	
	private class TestController extends QuestionController {
		public TestController(DeviceUser user, CacheDataManager cache, ESDataManager es) {
			super(user, cache, es);
		}
	}
	
	public void testSorting() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final List<ArrayList<Question>> results = new ArrayList<ArrayList<Question>>();

		DeviceUser user = new TestDeviceUser();
		CacheDataManager cache = new TestCacheManager();
		final ESDataManager es = new TestESManager();
		QuestionController controller = new TestController(user, cache, es);

		controller.addQuestion("Question 1", "ignore pls", null, false);
		controller.addQuestion("Question 2", "talk pls", null, false);
		controller.addQuestion("Question 3", "the claw", null, false);

		// wait for it to be uploaded
		signal.await(2, TimeUnit.SECONDS);

		// check that the question was pushed to the ES server
		runTestOnUiThread(new Runnable() { public void run() {
				results.add((ArrayList<Question>)es.getQuestions());
			}
		});
		
		// wait for the response
		signal.await(2, TimeUnit.SECONDS);
		
		List<Question> questions = results.get(0);
		
		Long id0 = questions.get(0).getID();
		Long id1 = questions.get(1).getID();
		Long id2 = questions.get(2).getID();
		

		for(int i = 0; i<10; i++) {
			questions.get(0).upvote();
		}
		for(int i = 0; i<100; i++) {
			questions.get(1).upvote();
		}
		for(int i = 0; i<500; i++) {
			questions.get(2).upvote();
		}
		questions = (new QuestionSorter()).sort(questions);


		assertTrue("Correct count", questions.size() == 3);
		assertTrue("500 Votes not first", questions.get(0).getID() == id2);
		assertTrue("200 Votes not second", questions.get(1).getID().equals(id1));
		assertTrue("10 Vote not third", questions.get(2).getID() == id0);
	}
}