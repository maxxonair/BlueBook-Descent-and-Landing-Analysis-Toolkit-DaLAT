package VisualEngine.terrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import VisualEngine.models.RawModel;
import VisualEngine.renderEngine.Loader;
import VisualEngine.textures.TerrainTexture;
import VisualEngine.textures.TerrainTexturePack;
import VisualEngine.toolbox.Maths;

public class Terrain {
	
	private static final float SIZE = 800;
	private static final float MAX_HEIGHT = 40;
	private static final float MAX_PIXEL_COLOUR = 256 * 256 * 256;
	
	private float x;
	private float z;
	private RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	
	private float[][] heights;
	
	
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = generateTerrain(loader, heightMap);
	}
	
	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	public TerrainTexture getBlendMap() {
		return blendMap;
	}

	private RawModel generateTerrain(Loader loader, String heightMap){
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("VisualEngine/heightMap/"+heightMap+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int VERTEX_COUNT =  image.getHeight(); 
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		int count = (int) (VERTEX_COUNT * VERTEX_COUNT);
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[(int) (6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1))];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				float height = getHeight( j, i, image);
				vertices[vertexPointer*3+1] = height;
				heights[i][i]=height;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormal(j,i,image);
				normals[vertexPointer*3]   = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.z;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (int) ((gz*VERTEX_COUNT)+gx);
				int topRight = topLeft + 1;
				int bottomLeft = (int) (((gz+1)*VERTEX_COUNT)+gx);
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	public float getHeightOfTerrain(float x, float z) {
		float terrainX = x - this.x;
		float terrainZ = z - this.z;
		float gridSquareSize =  SIZE / ((float)heights.length-1);
		int gridX = (int) Math.floor(terrainX/gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ/gridSquareSize);
		if(gridX >= (heights.length-1) || gridZ >= (heights.length-1) || gridX < 0 || gridZ < 0) {
			return 0;
		}
		float xCoord = (terrainX % gridSquareSize)/gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize)/gridSquareSize;
		float height;
		if (xCoord <= (1-zCoord)) {
			height = Maths
					.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			height = Maths
					.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}
	
	private Vector3f calculateNormal(int x, int z, BufferedImage image) {
		 if(x<0 || x>=image.getHeight() || z<0 || z>=image.getHeight()) {
			 Vector3f zero = new Vector3f(0,1,0);
			 return zero;
		 } 
		float heightL = getHeight(x-1,z, image);
		float heightR  = getHeight(x+1,z, image);
		float heightU = getHeight(x,z+1, image);
		float heightD = getHeight(x,z-1, image);
		Vector3f normal = new Vector3f(heightL-heightR, 2, heightD-heightU); 
		normal.normalise();
		return normal; 
	}

	private float getHeight(int x, int y, BufferedImage image) {
		 if(x<0 || x>=image.getHeight() || y<0 || y>=image.getHeight()) {
			 return 0;
		 } 
		float height = image.getRGB( x, y);	 
		height += MAX_PIXEL_COLOUR/2f; 
		height /= MAX_PIXEL_COLOUR/2f;
		height *= MAX_HEIGHT;
		return height; 
	}
}
