package photo.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import photo.components.Album;
import photo.components.Photo;
import photo.components.Tag;
import photo.components.User;

/**
 * This class defines a LoginSceneController
 * @author mtr103
 * @author kkb62
 */
public class LoginSceneController implements Initializable {
	
	/** Store the list of users in the choice box */
	@FXML ChoiceBox<User> choiceB_users;
	
	/** Store the login button */
	@FXML Button btn_login;
	
	/** Store the exit button */
	@FXML Button btn_exit;

	/** Store the list of users */
	private ArrayList<User> userList = new ArrayList<User>();

	/**
	 * Save a user to file system
	 * @param user A user
	 */
	public void saveUser(User user) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream("data/" + user.getName() + ".ser");
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(user);
		objectOutputStream.close();
		fileOutputStream.close();
	}
	
	/**
	 * Load a user from the file system
	 * @param filename A filename to a user save
	 */
	public User loadUser(String filename) throws ClassNotFoundException, IOException {
		User user;
		FileInputStream fileInputStream = new FileInputStream("data/" + filename);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
		user = (User) objectInputStream.readObject();
		objectInputStream.close();
		fileInputStream.close();
		
		return user;
	}
	
	/**
	 * Get a list of files in the data path
	 * @return A file list
	 */
	public String[] getFileNameListOfSavedUserData() {
		File file = new File("data");
		file.list(
				new FilenameFilter() {
					@Override
					public boolean accept(File f, String name) {
						return name.endsWith(".ser");
					}});

		return file.list();	
	}
	
	/**
	 * Load a file list from file system
	 * @param filenameList A list of user file names
	 */
	public void loadUserDataFromFileNameList(String [] filenameList) {
		for (String file : filenameList) {
			try {
				User user = loadUser(file);
				this.userList.add(user);
			}
			catch (ClassNotFoundException e) {} 
			catch (IOException e) {}
		}
	}
	
	/**
	 * Load users from file system into choice box of users
	 */
	public void load() throws IOException {	
		String[] fileNameList = getFileNameListOfSavedUserData();
		loadUserDataFromFileNameList(fileNameList);
		choiceB_users.getItems().addAll(userList);
	}
	
	/**
	 * Save all users to the file system
	 */
	public void saveAll() {
		ObservableList<User> users; 
		users = choiceB_users.getItems();
		
		for (User user : users) {
			try {
				saveUser(user);
			} catch (IOException e) {
			}
		}
	}
	
	/**
	 * Handle for the login action event
	 * @param event ActionEvent
	 */
	public void handleLoginButtonAction(ActionEvent event) throws FileNotFoundException {
		User selectedUser = choiceB_users.getValue();
		
		if (selectedUser != null) {
			if (selectedUser.getName().equals("Admin")) {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/photo/view/ui_administration.fxml"));
				Parent root = null;
				
				try  {
					root = loader.load();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if (root != null) {
					Scene scene = new Scene(root);
	
					AdministrationController adminController = loader.getController();	
					adminController.setPreviousScene(btn_login.getScene());
					adminController.load(choiceB_users);
	
					Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					stage.setScene(scene);
					stage.show();
				} 
			} else {	
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/photo/view/ui_album.fxml"));
				Parent root = null;
			
				try 
				{
					root = loader.load();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
	
				if (root != null)
				{
					User u = choiceB_users.getSelectionModel().getSelectedItem();
					if (u.getAlbums() == null) {
						u.setAlbums(new ArrayList<Album>());
					}
					
					Scene scene = new Scene(root);
											
					AlbumSceneController albumSceneController = loader.getController();			
					albumSceneController.setPreviousScene(btn_login.getScene());
					albumSceneController.setCurrentUser(u);
					albumSceneController.setAlbumsList(u.getAlbums());
					
					// Store a back reference to this scene's controller. Helpful!
					scene.setUserData(albumSceneController);
			
					Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					stage.setScene(scene);
					stage.show();
				}
			}
		}
	}
	
	/**
	 * Get the users of a choice box
	 * @return List of Choice Box users
	 */
	public ChoiceBox<User> getChoiceB_users() {
		return choiceB_users;
	}

	/**
	 * Handle for the exit action event
	 * @param event ActionEvent
	 */
	public void handleExitButtonAction(ActionEvent event) {
		if (btn_exit != null) {
			Stage stage = (Stage) btn_exit.getScene().getWindow();
			if (stage != null) {
				ButtonType btn_yes = new ButtonType("Yes", ButtonData.OK_DONE);
				ButtonType btn_no = new ButtonType("No", ButtonData.CANCEL_CLOSE);
				
				Alert alert = new Alert(AlertType.CONFIRMATION, "", btn_yes, btn_no);
				alert.initStyle(StageStyle.UTILITY);
				alert.setHeaderText("Do you wish to exit?");

				Optional<ButtonType> response = alert.showAndWait();

				if (response.get() == btn_yes) {
					saveAll();
					stage.close();
				}
			}
		}
	}

	/**
	 * Initialize the controller
	 * @param arg0 URL
	 * @param arg1 Resource Bundle 
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		User admin = new User("Admin", 1);
		try {
			saveUser(admin);
		} catch (IOException e) {
			
		}
	
		User stock = new User("Stock", 1);
		Album defaultAlbum = new Album("stock", stock);
		
		Photo p1 = new Photo("photo1.jpg", "data/", "Mother and Son", defaultAlbum);
		p1.addTag(new Tag("Location", "Italy"));
		p1.addTag(new Tag("Person", "Michelle"));
		p1.addTag(new Tag("Person", "Gino"));
		defaultAlbum.addPhoto(p1);
		
		Photo p2 = new Photo("photo2.jpg", "data/", "Fun at the office", defaultAlbum);
		p2.addTag(new Tag("Location", "Office"));
		p2.addTag(new Tag("Person", "Claire"));
		p2.addTag(new Tag("Person", "James"));
		defaultAlbum.addPhoto(p2);
		
		Photo p3 = new Photo("photo3.jpg", "data/", "Look at this", defaultAlbum);
		p3.addTag(new Tag("Location", "Office"));
		defaultAlbum.addPhoto(p3);
		
		Photo p4 = new Photo("photo4.jpg", "data/", "A good result", defaultAlbum);
		p4.addTag(new Tag("Location", "Hospital"));
		p4.addTag(new Tag("Person", "Anya"));
		defaultAlbum.addPhoto(p4);
		
		Photo p5 = new Photo("photo5.jpg", "data/", "Strike a pose", defaultAlbum);
		p5.addTag(new Tag("Location", "Studio"));
		p5.addTag(new Tag("Person", "Brandon"));
		defaultAlbum.addPhoto(p5);
		
		Photo p6 = new Photo("photo6.jpg", "data/", "Piggyback ride", defaultAlbum);
		p6.addTag(new Tag("Location", "Park"));
		defaultAlbum.addPhoto(p6);
		
		Photo p7 = new Photo("photo7.jpg", "data/", "Something new", defaultAlbum);
		p7.addTag(new Tag("Location", "New York City"));
		defaultAlbum.addPhoto(p7);
		
		Photo p8 = new Photo("photo8.jpg", "data/", "Cheers", defaultAlbum);
		p8.addTag(new Tag("Location", "Restaurant"));
		defaultAlbum.addPhoto(p8);
		
		Photo p9 = new Photo("photo9.jpg", "data/", "Smile", defaultAlbum);
		p9.addTag(new Tag("Location", "Rome"));
		p9.addTag(new Tag("Person", "Claire"));
		defaultAlbum.addPhoto(p9);
		
		Photo p10 = new Photo("photo10.jpg", "data/", "Howdy", defaultAlbum);
		p10.addTag(new Tag("Location", "Farm"));
		p10.addTag(new Tag("Person", "Stephanie"));
		defaultAlbum.addPhoto(p10);
	
		stock.addAlbum(defaultAlbum);
		
		try {
			saveUser(stock);
		} catch (IOException e) {

		}
		
	}
}
