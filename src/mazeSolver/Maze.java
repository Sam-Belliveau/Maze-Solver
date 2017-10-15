package mazeSolver;

import java.awt.Color;
import java.awt.image.BufferedImage;

enum direct { none, up, down, left, right}

public class Maze {

	public int size = 10;
	
	public int[] start = {0, 0};
	public int[] end = {(this.size-1), (this.size-1)};

	public boolean isPath = false;
	
	public boolean[][] walls = new boolean[this.size][this.size];
	
	public direct[][] squarePath = new direct[this.size][this.size];
	
	public boolean[][] isDud = new boolean[this.size][this.size];
	
	public boolean[][] inPath = new boolean[this.size][this.size];
	
	public int[][] colorT = new int[this.size][this.size];
	
	public Maze(){
		reset();
	} 
	
	public Maze(int s){
		this.size = s;
		reset();
	}
	
	public void reset(){
		this.squarePath = new direct[this.size][this.size];
		this.walls = new boolean[this.size][this.size];
		this.inPath = new boolean[this.size][this.size];
		this.isDud = new boolean[this.size][this.size];
		this.colorT = new int[this.size][this.size];
		
		this.start[0] = 0; 	this.start[1] = 0; 
		this.end[0] = this.size-1;this.end[1] = this.size-1; 
		
		for(int x = 0; x < this.size; x++){
			for(int y = 0; y < this.size; y++){
				this.squarePath[x][y] = direct.none;
			}
		}
	}
	
	public void softReset(){
		this.isPath = false;
		
		this.walls[start[0]][start[1]] = false;
		this.walls[end[0]][end[1]] = false;
		
		this.squarePath = new direct[this.size][this.size];
		this.inPath = new boolean[this.size][this.size];
		this.isDud = new boolean[this.size][this.size];
		this.colorT = new int[this.size][this.size];
		
		for(int x = 0; x < this.size; x++){
			for(int y = 0; y < this.size; y++){
				this.colorT[x][y] = 0;
				this.isDud[x][y] = false;
				this.inPath[x][y] = false;
				this.squarePath[x][y] = direct.none;
			}
		}
	}
	
	public void solve(boolean saveImage, BufferedImage img){
		boolean done = false;
		boolean stopped = false;
		this.isPath = false;
		
		softReset();
		
		while(!stopped){
			done = false;
			stopped = true;
			for(int x = 0; x < this.size && !done; x++){
				for(int y = 0; y < this.size && !done; y++){
					
					if(!this.walls[x][y] && !this.isDud[x][y]){
						if(this.squarePath[x][y] != direct.none || (x == this.start[0] && y == this.start[1])){
							stopped = false;
							for(int pX = -1; pX < 2; pX+=2){
								try{
									if(this.squarePath[x+pX][y] == direct.none && !this.walls[x+pX][y]){
										if(pX == -1){
											this.squarePath[x+pX][y] = direct.right;
										} else {
											this.squarePath[x+pX][y] = direct.left;
										}
									} 
									if(this.end[0] == (x+pX) && this.end[1] == y){
										done = true;
										break;
									}
								} catch (Exception e){
									
								}
							}
							for(int pY = -1; pY < 2; pY+=2){
								try{
									if(this.squarePath[x][y+pY] == direct.none && !this.walls[x][y+pY]){
										if(pY == -1){
											this.squarePath[x][y+pY] = direct.down;
										} else {
											this.squarePath[x][y+pY] = direct.up;
										}
									}
									if(this.end[0] == (x) && this.end[1] == y+pY){
										done = true;
										break;
									}
								} catch (Exception e){
									
								}
							}
							if(!done){
								this.isDud[x][y] = true;
							}
						}
					}
				}
			}
			if(done){
				this.isPath = true;
				int move = 0;
				int x = end[0];
				int y = end[1];
				while(x != start[0] || y != start[1]){
					this.colorT[x][y] = move;
					this.inPath[x][y] = true;
					if(this.squarePath[x][y] == direct.up){
						y--;
					} else if (this.squarePath[x][y] == direct.down){
						y++;
					} else if (this.squarePath[x][y] == direct.left){
						x--;
					} else if (this.squarePath[x][y] == direct.right){
						x++;
					} 
					move++;
				} //this.colorT[x][y] = move;
				
				for(int ax = 0; ax < this.size; ax++){
					for(int ay = 0; ay < this.size; ay++){
						if(this.inPath[ax][ay]){
							this.colorT[ax][ay] = (int) Math.round(Math.max(0, Math.min(this.colorT[ax][ay]*256/move, 255)));
							if(saveImage){
								img.setRGB(ax, ay, new Color((this.colorT[ax][ay]),0,(255-this.colorT[ax][ay])).getRGB());
							}
						}
					}
				}
				
				return;
		}
		this.isPath = false;
		
		}
		return;
	}
	
}
