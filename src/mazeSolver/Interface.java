package mazeSolver;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.List;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class Interface extends JFrame{
	
	public JLabel outImage = new JLabel();
	
	public BufferedImage boardImage;
	
	public BufferedImage tempImage;
	
	public String Iname = "";
	
	public JButton solve = new JButton("Solve"),
			solveNSave = new JButton("Solve And Save");
	
	public Maze maze = new Maze(20);

	public String font = "Calibri";

	public JLabel sizeText = new JLabel("Grid Size:"), saveText = new JLabel("Board Name:"),
			info = new JLabel("<html><b>HINT:</b><i> drag and drop images to input image mazes!</i></html>");
	
	public JTextField size = new JTextField(20), saveName = new JTextField("Maze Name");
	
	public JButton reset = new JButton("Reset Grid!"),
			save = new JButton("Save Board!"), load = new JButton("Load Board!");
			
	public JComboBox<String> draw = new JComboBox<String>();
	
	public int imageSize = 800;

	protected boolean painting;
	
	public Interface(){
		getContentPane().setBackground(new Color(240,240,240));
		maze.reset();
		
		setTitle("Maze Solver");
		getContentPane().setLayout(null);
		updateBoard();
		
		/*** BOARD ***/
		
		outImage.setTransferHandler( createTransferHandler() );
		
		outImage.setBounds(0, 0, imageSize, imageSize);
		add(outImage);
		
		solve.setFont(new Font(font, Font.BOLD, 40));
		solve.addActionListener(new ActionListener() {
			   @Override
			   public void actionPerformed(ActionEvent e) {
					maze.solve(false, null);

					updateBoard();
			   }
			});
		solve.setBounds(825, 25, 200, 50);
		add(solve);
		
		solveNSave.setFont(new Font(font, Font.BOLD, 24));
		solveNSave.addActionListener(new ActionListener() {
			   @Override
			   public void actionPerformed(ActionEvent e) {
				   BufferedImage temp = new BufferedImage(maze.size, maze.size, BufferedImage.TYPE_INT_RGB);
				   for(int x = 0; x < maze.size; x++){
					   for(int y = 0; y < maze.size; y++){
						   if(maze.walls[x][y]){
							   temp.setRGB(x, y, Color.black.getRGB());
						   } else {
							   temp.setRGB(x, y, Color.white.getRGB());
						   }
					   }
				   }
				   maze.solve(true, null);
				   File outputfile = new File(saveName.getText() + " (SOLVED).png");
				    try {
						ImageIO.write(temp, saveName.getText() + "png", outputfile);
					} catch (IOException q) {
						// TODO Auto-generated catch block
						q.printStackTrace();
					}

					updateBoard();
			   }
			});
		solveNSave.setBounds(825, 75, 200, 50);
		add(solveNSave);
		
		sizeText.setBounds(825, 150, 200, 50);
		sizeText.setFont(new Font(font, Font.BOLD, 30));
		sizeText.setHorizontalAlignment(JLabel.CENTER);
		add(sizeText);
		
		size.setBounds(825, 200, 200, 50);
		size.setFont(new Font(font, Font.PLAIN, 24));
		size.setText("15");
		add(size);
		
		reset.setBounds(825, 275, 200, 75);
		reset.setFont(new Font(font, Font.BOLD, 30));
		reset.addActionListener(new ActionListener() {
			   @Override
			   public void actionPerformed(ActionEvent e) {
				   try{
				   		maze.size = Integer.valueOf(size.getText());
				   } catch(Exception q){
					   	maze.size = 10;
				   }
				   
				   maze.reset();
					updateBoard();
			   }
			});
		add(reset);
		
		String[] options = {"Draw","Erase","Move Start", "Move End"};
		draw = new JComboBox<String>(options);
		draw.setBounds(825, 400, 200, 50);
		draw.setFont(new Font(font, Font.BOLD, 28));
		add(draw);
		
		info.setBounds(825, 450, 200, 125);
		info.setFont(new Font(font, Font.ITALIC, 18));
		info.setHorizontalAlignment(JLabel.CENTER);
		add(info);
		
		saveText.setBounds(825, 575, 200, 50);
		saveText.setFont(new Font(font, Font.BOLD, 30));
		saveText.setHorizontalAlignment(JLabel.CENTER);
		add(saveText);
		
		saveName.setBounds(825, 625, 200, 50);
		saveName.setFont(new Font(font, Font.ITALIC, 24));
		saveName.setText("World Name");
		add(saveName);
		
		save.setBounds(825, 685, 200, 40);
		save.setFont(new Font(font, Font.BOLD, 20));
		save.addActionListener(new ActionListener() {
			   @Override
			   public void actionPerformed(ActionEvent e) {
				   save();
			   }
			   });
		add(save);
		
		load.setBounds(825, 735, 200, 40);
		load.setFont(new Font(font, Font.BOLD, 20));
		load.addActionListener(new ActionListener() {
			   @Override
			   public void actionPerformed(ActionEvent e) {
				   load();
			   }
			});
		add(load);
		
		setSize(1050,840);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		
		getContentPane().addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
            	int roundX = Math.round(e.getX()/((imageSize/maze.size)));
            	int roundY = Math.round(e.getY()/((imageSize/maze.size)));
                try{
                	@SuppressWarnings("unused")
					boolean someBool = maze.walls[roundX][roundY]; // Out Of Bounds Check
                	if(draw.getSelectedIndex() == 0){
                		maze.walls[roundX][roundY] = true;
                	} else if (draw.getSelectedIndex() == 1){
                		maze.walls[roundX][roundY] = false;
                	} else if (draw.getSelectedIndex() == 2){
                		maze.start[0] = roundX;
                		maze.start[1] = roundY;
                	} else {
                		maze.end[0] = roundX;
                		maze.end[1] = roundY;
                	}
            		maze.softReset();
                	updateBoard();
                } catch (ArrayIndexOutOfBoundsException a) {}
                painting = true;
            }

            public void mouseReleased(MouseEvent e) {
                painting = false;
            }
        });
		
		getContentPane().addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
            	if(painting){
	            	int roundX = Math.round(e.getX()/((imageSize/maze.size)));
	            	int roundY = Math.round(e.getY()/((imageSize/maze.size)));
	                try{
	                	@SuppressWarnings("unused")
						boolean someBool = maze.walls[roundX][roundY]; // Out Of Bounds Check
	                	if(draw.getSelectedIndex() == 0){
	                		maze.walls[roundX][roundY] = true;
	                	} else if (draw.getSelectedIndex() == 1){
	                		maze.walls[roundX][roundY] = false;
	                	} else if (draw.getSelectedIndex() == 2){
	                		maze.start[0] = roundX;
	                		maze.start[1] = roundY;
	                	} else {
	                		maze.end[0] = roundX;
	                		maze.end[1] = roundY;
	                	}
	            		maze.softReset();
	                	updateBoard();
	                } catch (ArrayIndexOutOfBoundsException a) {}
            	}
            }

            public void mouseMoved(MouseEvent e) {
            	if(painting){
	            	int roundX = Math.round(e.getX()/((imageSize/maze.size)));
	            	int roundY = Math.round(e.getY()/((imageSize/maze.size)));
	                try{
	                	@SuppressWarnings("unused")
						boolean someBool = maze.walls[roundX][roundY]; // Out Of Bounds Check
	                	if(draw.getSelectedIndex() == 0){
	                		maze.walls[roundX][roundY] = true;
	                	} else if (draw.getSelectedIndex() == 1){
	                		maze.walls[roundX][roundY] = false;
	                	} else if (draw.getSelectedIndex() == 2){
	                		maze.start[0] = roundX;
	                		maze.start[1] = roundY;
	                	} else {
	                		maze.end[0] = roundX;
	                		maze.end[1] = roundY;
	                	}
	            		maze.softReset();
	                	updateBoard();
	                } catch (ArrayIndexOutOfBoundsException a) {}
            	}
            }
        });
	}
	
	private TransferHandler createTransferHandler(){
	    return new TransferHandler() {
	        @Override public boolean canImport(JComponent component, DataFlavor[] flavors) {
	            return true;
	          }
	          @Override public boolean importData(JComponent component, Transferable transferable) {
	            try {
	              for (DataFlavor flavor : transferable.getTransferDataFlavors()) {
	                System.out.println(flavor);
	                if (DataFlavor.imageFlavor.equals(flavor)) {
	                  Object o = transferable.getTransferData(DataFlavor.imageFlavor);
	                  if (o instanceof Image) {
	                    tempImage = (BufferedImage) o;
	    	            importImage();
	                    return true;
	                  }
	                }
	                if (DataFlavor.javaFileListFlavor.equals(flavor)) {
	                  Object o = transferable.getTransferData(DataFlavor.javaFileListFlavor);
	                  if (o instanceof List) {
	                    List list = (List) o;
	                    for (Object f : list) {
	                      if (f instanceof File) {
	                        File file = (File) f;
                        	tempImage = (BufferedImage) ImageIO.read(file);
          	            	Iname = file.getName();
          	            	importImage();
          	            	return true;
	                      }
	                    }
	                  }
	                }
	              }
	            } catch (Exception ex) {
	              ex.printStackTrace();
	            }
	            return false;
	          }
	          @Override public int getSourceActions(JComponent component) {
	            return COPY;
	          }
	        };
	  }
	
	public void save() {
		try{
			FileOutputStream stream = new FileOutputStream(new File(saveName.getText() + "(MAZE).bin"));
			boolean[] board1D = new boolean[maze.size*maze.size];
		   
			for(int i = 0; i < maze.size; i++){
			   for(int ii = 0; ii < maze.size; ii++){
				   board1D[(i*maze.size)+ii] = maze.walls[i][ii];
			   }
			}
		   
			
			/* START AND STOP POSITIONS */
			stream.write(maze.start[0]%128);
			stream.write((maze.start[0]-(maze.start[0]%128)) << 7);
			stream.write(maze.start[1]);
			stream.write((maze.start[1]-(maze.start[1]%128)) << 7);
			stream.write(maze.end[0]);
			stream.write((maze.end[0]-(maze.end[0]%128)) << 7);
			stream.write(maze.end[1]);
			stream.write((maze.end[1]-(maze.end[1]%128)) << 7);
			
			/* WALL POSITIONS */
			for (boolean item : board1D)
			{
		       stream.write(item ? 1 : 0);
			}
			stream.close();
			updateBoard();
		} catch(IOException e){}
	}
	
	public void load() {
		try{
			maze.reset();
		    File file = new File(saveName.getText() + "(MAZE).bin");
		    FileInputStream inputStream = new FileInputStream(file);
		    int fileLength = (int) file.length();

		    byte[] data = new byte[fileLength];
		    boolean[] temp = new boolean[fileLength-8];
		    
		    inputStream.read(data);
		    maze.start[0] = data[0] + (data[1] >> 7);
		    maze.start[1] = data[2] +(data[3] >> 7);
		    maze.end[0] = data[4] + (data[5] >> 7);
		    maze.end[1] = data[6] + (data[7] >> 7);
		    for (int X = 8; X < data.length; X++)
		    {
		        if (data[X] != 0) {
		        	temp[X-8] = true;
		        } else {
		        	temp[X-8] = false;
		        }
		    }
		    
		    maze.walls = new boolean[(int) Math.sqrt(temp.length)][(int) Math.sqrt(temp.length)];
		    maze.size = (int) Math.sqrt(temp.length);
		    size.setText(String.valueOf((int) Math.sqrt(temp.length)));
		    for(int i = 0; i < Math.sqrt(temp.length); i++){
				for(int ii = 0; ii < Math.sqrt(temp.length); ii++){
					maze.walls[i][ii] = temp[(int) ((i*Math.sqrt(temp.length))+ii)];
				}
			}

			updateBoard();
		    inputStream.close();
		} catch(Exception e){
			try{
				maze.walls = new boolean[Integer.valueOf(size.getText())][Integer.valueOf(size.getText())];
			} catch (Exception q) {
				maze.walls = new boolean[15][15];
			}
			updateBoard();
		}
	}

	public void updateBoard(){
		maze.softReset();
		Color white = new Color(255, 255, 255);
		Color blue = new Color(0, 0, 255);
		Color red = new Color(255, 0, 0);
		Color black = new Color(0, 0, 0);
		Color grey = new Color(240, 240, 240);
		Color grey2 = new Color(248, 248, 248);
		
		boardImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < imageSize; x++){
			for(int y = 0; y < imageSize; y++){
				try{
					int tX, tY;
					try{
						tX = Math.round(x/(imageSize/maze.size)); tY = Math.round(y/(imageSize/maze.size));
					} catch (ArithmeticException t){
						maze.size = 20;
						maze.reset();
						tX = Math.round(x/(imageSize/maze.size)); tY = Math.round(y/(imageSize/maze.size));
					}
					boolean temp = maze.walls[tX][tY];
					
					if(x%(imageSize/maze.size) == 0 || y%(imageSize/maze.size) == 0){
						if(maze.start[0] == tX && maze.start[1] == tY){
							 boardImage.setRGB(x, y, red.getRGB()); 
						} else if (maze.end[0] == tX && maze.end[1] == tY){
							 boardImage.setRGB(x, y, blue.getRGB()); 
						} else if (temp){
							boardImage.setRGB(x, y, black.getRGB());
						} else if (maze.inPath[tX][tY]){
							boardImage.setRGB(x, y, new Color((maze.colorT[tX][tY]),0,(255-maze.colorT[tX][tY])).getRGB());
						} else {
							boardImage.setRGB(x, y, grey.getRGB());
						}
					} else if((maze.start[0] == tX && maze.start[1] == tY)){
						boardImage.setRGB(x, y, red.getRGB());
					} else if(maze.end[0] == tX && maze.end[1] == tY){
						boardImage.setRGB(x, y, blue.getRGB());
					} else if(temp){
						boardImage.setRGB(x, y, black.getRGB());
					} else if(maze.inPath[tX][tY]){ 
						boardImage.setRGB(x, y, new Color((maze.colorT[tX][tY]),0,(255-maze.colorT[tX][tY])).getRGB());
					} else {
						boardImage.setRGB(x, y, white.getRGB());
					}
				} catch (ArrayIndexOutOfBoundsException e){boardImage.setRGB(x, y, grey2.getRGB());}
			}
		}
		outImage.setIcon(new ImageIcon(boardImage));
	}
	
	public void importImage(){
		BufferedImage tempTempImage = this.tempImage;
	    size.setText(String.valueOf(this.tempImage.getWidth()));
	    maze.reset();
		if(this.tempImage.getHeight() <= this.imageSize && this.tempImage.getWidth() <= this.imageSize){
			maze.size = Math.max(this.tempImage.getWidth(), this.tempImage.getHeight());
			maze.reset();
			for(int x = 0; x < this.tempImage.getWidth(); x++){
				for(int y = 0; y < this.tempImage.getHeight(); y++){
					int rgb = this.tempImage.getRGB(x, y);
					int r = (rgb >> 16) & 0xFF;
					int g = (rgb >> 8) & 0xFF;
					int b = (rgb & 0xFF);
					int gray = (r + g + b) / 3;
					if(gray <= 128){
						maze.walls[x][y] = true;
					} else {
						maze.walls[x][y] = false;
					}
				}
			}
		} else {
			maze.size = Math.max(this.tempImage.getWidth(), this.tempImage.getHeight());
			maze.reset();
		    this.tempImage = scaleImage(tempTempImage, 800, 800);
			for(int x = 0; x < this.tempImage.getWidth(); x++){
				for(int y = 0; y < this.tempImage.getHeight(); y++){
					int rgb = this.tempImage.getRGB(x, y);
					int r = (rgb >> 16) & 0xFF;
					int g = (rgb >> 8) & 0xFF;
					int b = (rgb & 0xFF);
					int gray = (r + g + b) / 3;
					if(gray < 128){
						maze.walls[x][y] = true;
					} else {
						maze.walls[x][y] = false;
					}
				}
			}
		    importImage();
		} updateBoard();
		this.tempImage = null;
	}

	
	public BufferedImage scaleImage(BufferedImage before, int nW, int nH){
		BufferedImage resized = new BufferedImage(nW, nH, before.getType());
		Graphics2D g = resized.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); 
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(before, 0, 0, nW, nH, 0, 0, before.getWidth(), before.getHeight(), null);
		g.dispose();
		return resized;
	}
	
}
