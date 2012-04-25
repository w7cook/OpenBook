package unit;

import org.junit.*;

import controllers.Application;

import java.util.*;
import play.test.*;
import utils.Bootstrap;
import models.*;

public class BasicTest extends UnitTest {

	@Before
	public void setup() {
		Fixtures.deleteDatabase();
	}

	@Test
	public void createAndRetrieveUser() {
		// Create a new user and save it
		new User("bob@gmail.com", "secret", "Bob").save();

		// Retrieve the user with e-mail address bob@gmail.com
		User bob = User.find("byEmail", "bob@gmail.com").first();

		// Test
		assertNotNull(bob);
		assertEquals("Bob", bob.username);
	}

	@Test
	public void tryConnectAsUser() {
		// Create a new user and save it
		new User("bob@gmail.com", "secret", "Bob").save();

		// Test
		assertNotNull(User.connect("bob@gmail.com", "secret"));
		assertNull(User.connect("bob@gmail.com", "badpassword"));
		assertNull(User.connect("tom@gmail.com", "secret"));
	}

	@Test
	public void createPost() {
		// Create a new user and save it
		User bob = new User("bob@gmail.com", "secret", "Bob").save();

		// Create a new post
		new Post(bob, bob, "Hello world").save();

		// Test that the post has been created
		assertEquals(1, Post.count());

		// Retrieve all posts created by Bob
		List<Post> bobPosts = Post.find("byAuthor", bob).fetch();

		// Tests
		assertEquals(1, bobPosts.size());
		Post firstPost = bobPosts.get(0);
		assertNotNull(firstPost);
		assertEquals(bob, firstPost.owner);
		assertEquals("Hello world", firstPost.content);
		assertNotNull(firstPost.createdAt);
	}

	@Test
	public void testSigOther() {
		// Create a new user and save it
		User bob = new User("bob@gmail.com", "secret", "Bob").save();
		User alice = new User("alice@gmail.com", "secret", "Alice");
		alice.save();
		alice.profile.significantOther = bob;
		alice.save();

		User a2 = User.find("byUsername", "Alice").first();
		assertEquals("Bob", a2.profile.significantOther.username);
		
	}

	@Test
	public void postComments() {
		// Create some new users and save them
		User bob = new User("bob@gmail.com", "secret", "Bob").save();
    User jeff = new User("jeff@gmail.com", "secret", "Jeff").save();
    User tom = new User("tom@gmail.com", "secret", "Tom").save();

		// Create a new post
		Post bobPost = new Post(bob, bob, "Hello world").save();

		// Post a first comment
		new Comment(bobPost, jeff, "Nice post").save();
		new Comment(bobPost, tom, "I knew that !").save();

		// Retrieve all allComments
		List<Comment> bobPostComments = Comment.find("byParentObj", bobPost).fetch();

		// Tests
		assertEquals(2, bobPostComments.size());

		Comment firstComment = bobPostComments.get(0);
		assertNotNull(firstComment);
		assertEquals("Jeff", firstComment.owner.username);
		assertEquals("Nice post", firstComment.content);
		assertNotNull(firstComment.createdAt);

		Comment secondComment = bobPostComments.get(1);
		assertNotNull(secondComment);
		assertEquals("Tom", secondComment.owner.username);
		assertEquals("I knew that !", secondComment.content);
		assertNotNull(secondComment.createdAt);
	}
	
	@Test
	public void postStatus() {
	  // Create a new user and save it
		User bob = new User("bob@gmail.com", "secret", "Bob").save();
		User jeff = new User("jeff@gmail.com", "secret", "Jeff").save();
		
		new Status(bob, "I just had lunch").save();
		new Status(bob, "It wasn't too good").save();
		
		new Status(jeff, "Dude, I agree!").save();
		
		// Retrieve all status updates
		List<Status> allStatus = Status.findAll();
		
		// Retrieve all bob's status updates
		List<Status> bobStatuses = Status.find("byAuthor", bob).fetch();
		
		// Retrieve all jeff's status updates
		List<Status> jeffStatuses = Status.find("byAuthor", jeff).fetch();
		
		//Tests
		assertEquals(2, bobStatuses.size());
		assertEquals(1, jeffStatuses.size());
		assertEquals(3, allStatus.size());
		
		Status bobfirst = bobStatuses.get(0);
		assertNotNull(bobfirst);
		assertEquals("Bob", bobfirst.owner.username);
		assertEquals("I just had lunch", bobfirst.content);
		assertNotNull(bobfirst.createdAt);
	}

	@Test
	public void useTheCommentsRelation() {
    // Create a new user and save it
    User bob = new User("bob@gmail.com", "secret", "Bob").save();
    User jeff = new User("jeff@gmail.com", "secret", "Jeff").save();
    User tom = new User("tom@gmail.com", "secret", "Tom").save();

		// Create a new post
		Post bobPost = new Post(bob, bob, "Hello world").save();

		// Post a first comment
		bobPost.addComment(jeff, "Nice post");
		bobPost.addComment(tom, "I knew that !");

		// Count things
		assertEquals(3, User.count());
		assertEquals(1, Post.count());
		assertEquals(2, Comment.count());

		// Retrieve Bob's post
		bobPost = Post.find("byAuthor", bob).first();
		assertNotNull(bobPost);

		// Navigate to allComments
		assertEquals(2, bobPost.comments.size());
		assertEquals("Jeff", bobPost.comments.get(0).owner.username);

		// Delete the post
		bobPost.delete();

		// Check that all allComments have been deleted
		assertEquals(3, User.count());
		assertEquals(0, Post.count());
		assertEquals(0, Comment.count());
	}

	@Test
	public void fullTest() {
		Fixtures.loadModels("test-data.yml");
		Bootstrap.hashPasswords();

		// Count things
		assertEquals(3, User.count());
		assertEquals(3, Post.count());
		assertEquals(3, Comment.count());

		// Try to connect as users
		assertNotNull(User.connect("bob@gmail.com", "secret"));
		assertNotNull(User.connect("jeff@gmail.com", "secret"));
		assertNull(User.connect("jeff@gmail.com", "badpassword"));
		assertNull(User.connect("tom@gmail.com", "secret"));

		// Find all of Bob's posts
		List<Post> bobPosts = Post.find("author.email", "bob@gmail.com").fetch();
		assertEquals(2, bobPosts.size());

		// Find all allComments related to Bob's posts
		List<Comment> bobComments = Comment.find("parentObj.author.email",
				"bob@gmail.com").fetch();
		assertEquals(3, bobComments.size());

		// Find bob's first post
		Post frontPost = Post.find("byTitle", "About the model layer").first();
		assertNotNull(frontPost);

		// Check that this post has two allComments
		assertEquals(2, frontPost.comments.size());

		// Post a new comment
		frontPost.addComment((User)User.find("first_name = ?","Jeff").first(), "Hello guys");
		assertEquals(3, frontPost.comments.size());
		assertEquals(4, Comment.count());
	}

	@Test
	public void testTags() {
		// Create a new user and save it
		User bob = new User("bob@gmail.com", "secret", "Bob").save();

		// Create a new post
		new Post(bob, bob, "Hello world").save();
		new Post(bob, bob, "Hello world").save();
		
		List<Post> bobPosts = Post.find("byAuthor", bob).fetch();
		assertEquals(2, bobPosts.size());

	}	
}
