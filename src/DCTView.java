/******************************************************************************/
/****************************VIEW CONTROLLER***********************************/
/*************************Graphic User Interface*******************************/
/******************************************************************************/
/******************************************************************************/

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.*;

public class DCTView extends JFrame{
	

	private final DCTViewController controller; //back-end controller

	//Panels
	private JPanel northPanel; 
	private JPanel southPanel; 
	private JPanel originalImagePanel;
	private JPanel compressedImagePanel; 
	//Scroll panes
	private JScrollPane originalImageScrollPane;
	private JScrollPane compressedImageScrollPane;
	private final JSplitPane splitPane;
	//Text fields
	private JTextField fField; 
	private JTextField dField; 
	private JTextField nField; 
	//Buttons
	private JButton selectImageButton; 
	private JButton compressButton;

	private JButton evaluateDCTButton;
	private JButton executeButton;
	//Labels
	private JLabel originalImageLabel;
	private JLabel compressedImageLabel;
	private JLabel statusBarLabel;
	//Images
	private BufferedImage originalImage;
	private BufferedImage compressedImage;
	
	
	//Constructor
	public DCTView() {
		
		//Instantiate the back-end controller 
		this.controller = new DCTViewController(); 
		
		//Set window properties
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); //Maximize on start
		this.setDefaultCloseOperation(EXIT_ON_CLOSE); //Exit on close
		this.setTitle("DCT: Discrete Cosine Transform on Raster images");
		//Initialize components
		this.initializeTopBar();
		this.initializeOriginalImagePanel();
		this.initializeCompressedImagePanel();
		this.initializeBottomBar();
		//split the window
		this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); //Instantiate horizontal split pane
		this.splitPane.setLeftComponent(this.originalImageScrollPane);
		this.splitPane.setRightComponent(this.compressedImageScrollPane);
		this.splitPane.setResizeWeight(0.5);
		//Add components to the container
		this.add(this.northPanel, BorderLayout.NORTH);
		this.add(this.splitPane, BorderLayout.CENTER);
		this.add(this.southPanel, BorderLayout.SOUTH);
		
		this.initializeButtonAction();
		//disable A/B method buttons  
		this.compressButton.setEnabled(false);

		//enable test execute button
		this.executeButton.setEnabled(true);
		
		//materialize the window
		this.setVisible(true);
		
	}
	
	
	//Define button's actions
	private void initializeButtonAction() {
		
		//Select Image Button Listener
		this.selectImageButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				selectImage(e); //Call select image method
			}
			
		});
		
		//Compress Image Button Listener
		this.compressButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					compressImage(); //Call compression method
				}catch (NumberFormatException excp) {
					JOptionPane.showMessageDialog(null, "Input parameter is not a number!");
					statusBarLabel.setText("Input parameter is not a number!"); //Update status bar
				}catch (Exception excp2) {
					JOptionPane.showMessageDialog(null, excp2.getMessage());
					statusBarLabel.setText(excp2.getMessage()); //Update status bar
				}
			}
			
		});
		
		
		//Execute DCT2 on NxN matrix
		this.executeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					applyDCT(); //Call apply DCT method
					statusBarLabel.setText("Completed!"); //Update status bar
				}catch (NumberFormatException excp) {
					JOptionPane.showMessageDialog(null, "N input parameter is not a number!");
					statusBarLabel.setText("N input parameter is not a number!"); //Update status bar
				}
				
			}
			
		});
		
		//Execute DCT test button listener
		this.evaluateDCTButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				executeTest(); //Call execute DCT test method
				statusBarLabel.setText("Completed!"); //Update status bar
			}
			
		});
	}
	
	
	//Initialize top bar
	private void initializeTopBar() {
		
		this.northPanel = new JPanel();
		
		this.northPanel.add(createVerticalSeparator());
		//N field
		this.northPanel.add(new JLabel("N: "));
		this.nField = new JTextField(3);
		this.northPanel.add(this.nField);
		//Execute DCT on image button
		this.executeButton = new JButton("Execute DCT2");
		this.northPanel.add(this.executeButton);
		
		this.northPanel.add(createVerticalSeparator());
		//Select image button
		this.selectImageButton = new JButton("Select Image");
		this.northPanel.add(this.selectImageButton);
		//K Field
		this.fField = new JTextField(3);
		this.northPanel.add(new JLabel("F: "));
		this.northPanel.add(this.fField);
		this.fField.setEditable(false);
		//L Field
		this.dField = new JTextField(3);
		this.northPanel.add(new JLabel("d: "));
		this.northPanel.add(this.dField);
		this.dField.setEditable(false);
		//Compression button
		this.compressButton = new JButton("Compress");
		this.compressButton.setEnabled(true);
		this.northPanel.add(this.compressButton);

		
		this.northPanel.add(createVerticalSeparator());
		
		//Evaluate DCT2 button
		this.evaluateDCTButton = new JButton("Verify DCT2");
		this.northPanel.add(this.evaluateDCTButton);
		this.northPanel.add(createVerticalSeparator());
		
	}
	
	//Create vertical separator of top bar
    static JComponent createVerticalSeparator() {
		
        JSeparator x = new JSeparator(SwingConstants.VERTICAL);
        x.setPreferredSize(new Dimension(3,30));
        return x;
		
    }
	
	//Initialize left panel
	private void initializeOriginalImagePanel() {
		
		this.originalImagePanel = new JPanel(new FlowLayout());
		this.originalImageScrollPane = new JScrollPane(this.originalImagePanel);
		this.originalImagePanel.setBorder(BorderFactory.createEtchedBorder());
		this.originalImageLabel = new JLabel();
		this.originalImagePanel.add(this.originalImageLabel, BorderLayout.CENTER);
		
	}
	
	//Initialize right panel
	private void initializeCompressedImagePanel() {
		
		this.compressedImagePanel = new JPanel(new FlowLayout());
		this.compressedImageScrollPane = new JScrollPane(this.compressedImagePanel);
		this.compressedImagePanel.setBorder(BorderFactory.createEtchedBorder());
		this.compressedImageLabel = new JLabel();
		this.compressedImagePanel.add(this.compressedImageLabel, BorderLayout.CENTER);
		
	}
	
	//Initialize bottom bar
	private void initializeBottomBar() {
		
		this.southPanel = new JPanel();
		this.statusBarLabel = new JLabel("Status bar");
		this.southPanel.add(this.statusBarLabel);
		
	}
	
	
	//Action when pressing selectImage button 
	private void selectImage(ActionEvent e) {
		
		JFileChooser fe = new JFileChooser();
		int returnVal = fe.showOpenDialog(this.selectImageButton); //Pops up an "Open File" file chooser dialog
		
		if (returnVal == JFileChooser.APPROVE_OPTION) { //If file opens without problems
		
				File file = fe.getSelectedFile(); //get the file
				
				try { //Try reading the file stream
						FileInputStream fis = new FileInputStream(file);
						this.originalImage = ImageIO.read(fis); //returns a buffered image
				}catch (IOException ex) { 
					System.out.println("File read error occurred!"); 
					}

				this.originalImageLabel.setText(null);
				this.compressedImageLabel.setText(null);
				this.compressedImageLabel.setIcon(null);
				
				this.controller.setOriginalImageSize(file.length()); //give control to the controller - set image size 
				
				this.paintImage(this.originalImage); //paint original Image 
				
				this.paintDCT2Image(this.originalImage); //paint original Image after DCT2 

				this.dField.setEditable(true);  
				this.fField.setEditable(true); 
				this.compressButton.setEnabled(true); 
				
				this.statusBarLabel.setText("Image uploaded successfully!    Image Size: " + String.valueOf(this.originalImage.getWidth()) + "x" + String.valueOf(this.originalImage.getHeight())); //Update status bar
		}	
	}
	
	//Paint a BufferedImage
	private void paintImage(BufferedImage image) {
		this.originalImageLabel.setIcon(new ImageIcon(this.originalImage));
		this.originalImageLabel.setSize(this.originalImage.getWidth(), this.originalImage.getHeight());
		this.originalImagePanel.repaint();
	}
	
	//Paint a BufferedImage in DCT2 format
	private void paintDCT2Image(BufferedImage image) {
		this.compressedImage = this.controller.getDCT2Image(image); //give control to the controller
		this.compressedImageLabel.setIcon(new ImageIcon(this.compressedImage));
		this.compressedImageLabel.setSize(this.compressedImage.getWidth(), this.compressedImage.getHeight());
		this.compressedImagePanel.repaint();
	}
	
	//Image Compression Method
	private void compressImage() throws NumberFormatException, Exception{
		int d = 1;
		int F = 1;

		//Check if input is numeric
		if(this.isNumeric(this.dField.getText()) && this.isNumeric(this.fField.getText())) {
			F = Integer.parseInt(this.fField.getText());
			d = Integer.parseInt(this.dField.getText());
		}else
			throw new NumberFormatException();
		
		if (F <= this.originalImage.getWidth() && F <= this.originalImage.getHeight() && F > 0) { //check F value plausibility
			
			if (d <= (2*F-2) && d >= 0) { //check d value plausibility

				this.compressedImage = this.controller.compressImage(this.originalImage, F , d); //give control to the controller

				this.compressedImageLabel.setIcon(new ImageIcon(this.compressedImage));
				this.compressedImageLabel.setSize(this.compressedImage.getWidth(), this.compressedImage.getHeight());
				this.compressedImagePanel.repaint();
				
				this.selectImageButton.setEnabled(true);
				
				//Give control to the controller:
				//1.	Get OriginalImageSize (Kb)
				//2.	Get CompressedImageSize (Kb)
				//3.	Get Compression rate
				long dimensioneKBOriginal = this.controller.getOriginalImageSize() / 1024;
				long dimensioneKBCompressedImage = this.controller.getCompressedImageSize() / 1024;
				long rapportoCompressione = 100 - ((dimensioneKBCompressedImage * 100) / dimensioneKBOriginal);
				
				this.statusBarLabel.setText("Original Image Size: " + dimensioneKBOriginal + "KB" + " - " + String.valueOf(this.originalImage.getWidth()) + "x" + String.valueOf(this.originalImage.getHeight()) +
						"	| Compressed Image Size: " + dimensioneKBCompressedImage + "KB" + " - " + String.valueOf(this.compressedImage.getWidth()) + "x" + String.valueOf(this.compressedImage.getHeight()) +
						"	| Compression rate: " + rapportoCompressione + "%");
				this.dField.setEditable(true);
			}else {
				JOptionPane.showMessageDialog(null, "d value must be <= than (2F-2) and >= than 0");
				this.statusBarLabel.setText("d value must be <= than (2F-2) and >= than 0");
				}
				
		}else {
			JOptionPane.showMessageDialog(null, "F value must be <= than image's width/height and > 0");
			this.statusBarLabel.setText("F value must be <= than image's width/height and > 0");
		}
			
	}
	
	//Check for numeric input
	private boolean isNumeric(String string) {
		try {
			Integer.parseInt(string);
		} catch (NumberFormatException nfe) { 
			return false;
		}
		return true;
	}
	
	//Compare Jtransform/custom DCT2 methods
	private void applyDCT() throws NumberFormatException {
		int n;
		if(this.isNumeric(this.nField.getText())) {
			n = Integer.parseInt(this.nField.getText());
			this.controller.applyDCT(n); //give control to the controller, apply DCT
		}else
			throw new NumberFormatException();
	}
	
	//Execute a Jtransform/custom DCT2 test 
	private void executeTest() {
		
		String[] testString = this.controller.executeTest(); //return result strings
		this.compressButton.setEnabled(false);
		this.executeButton.setEnabled(true);
		this.originalImageLabel.setIcon(null);
		this.compressedImageLabel.setIcon(null);
		//print result strings
		this.originalImageLabel.setText(testString[0]);
		this.compressedImageLabel.setText(testString[1]);
		
	}

	
}
