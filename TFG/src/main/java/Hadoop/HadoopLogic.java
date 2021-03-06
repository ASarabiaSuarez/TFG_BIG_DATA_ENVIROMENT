package Hadoop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.apache.hadoop.conf.Configuration;


import EnviromentClass.AnalizerObject;

// TODO: Auto-generated Javadoc
/**
 * The Class HadoopLogic.
 */
public class HadoopLogic extends AnalizerObject  {
	private final Integer ONE = 1;
	private final String SUCCES = "_SUCCESS";
	private final String PDF = ".PDF";
	private final String ROOT = "/";
	/** The connection string. */
	private String connectionString ;
	
	/** The hdfscomands. */
	private HDFSLogic hdfscomands;
	
	/** The yarn. */
	private Yarn yarn;
	
	/** The folder input path. */
	private String folderInputPath;
	
	/** The folder outpath. */
	private String folderOutpath;
	
	/**
	 * Instantiates a new hadoop logic.
	 */
	public HadoopLogic() {
		setHdfscomands(new HDFSLogic());
		setYarn(new Yarn());
	}
	
	/**
	 * Instantiates a new hadoop logic.
	 *
	 * @param hdfsConfig the hdfs config
	 * @throws IOException 
	 */
	public HadoopLogic(String hdfsConnectionString ) throws IOException {
		setHdfscomands(new HDFSLogic(hdfsConnectionString));
		setYarn(new Yarn());
	}
	
	/**
	 * Execute.
	 *
	 * @param jarFilePathToExecute the jar file path to execute
	 * @param jarOption the jar option
	 */
	public void execute(String jarFilePathToExecute,String jarOption) {
		try {
			
			//System.out.println("fichero a ejecuta ruta " + getRemotePath()+getFileName());
			//System.out.println("folder de salida " + getOutputPath());
			executeToYarn(jarFilePathToExecute, jarOption, getRemotePath()+getFileName(), getOutputPath());
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteLocal() throws InterruptedException {
		try {
			Process auxProces = Runtime.getRuntime().exec("ls "+getFilePathToAnalize()+outputLocalFolder());
			System.out.println(" salida del ls "+getFilePathToAnalize()+outputLocalFolder());
			BufferedReader reader =  new BufferedReader(new InputStreamReader(auxProces.getInputStream()));
			String folder = reader.readLine();
			System.out.println(folder);
			if(reader.readLine() != null) {
				Runtime.getRuntime().exec("rm -r "+getFilePathToAnalize()+outputLocalFolder());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Execute to yarn.
	 *
	 * @param jarFilePathToExecute the jar file path to execute
	 * @param jarOption the jar option
	 * @param inputFolder the input folder
	 * @param outputFolder the output folder
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	public void executeToYarn( String jarFilePathToExecute, String jarOption, String inputFolder,String outputFolder) throws IOException, InterruptedException {	
		deleteLocal();
		getYarn().executeAlgorit(jarFilePathToExecute, jarOption, inputFolder, outputFolder);
	}
	
	/**
	 * Uplaod data.
	 *
	 * @param filePathToCluster the file path to cluster
	 */
	@Override
	public void uploadData(String filePathToCluster) {
		setFolderInputPath(filePathToCluster);
		try {
			if(getHdfscomands().existFile( getRemotePath(), getFileName())) {
				getHdfscomands().removeFolder(getRemotePath(), true);
			}
			getHdfscomands().copyFromLocal(getFilePathToAnalize(), getFileName(), getRemotePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Execute.
	 */
	@Override
	public void execute() {
		
	//	System.out.println("yarn jar ruta " + getYarn().getJarFileToExecute());
		//System.out.println("yarn jar option " + getYarn().getJarOption());
		execute(getYarn().getJarFileToExecute(), getYarn().getJarOption());
	}
	
	/**
	 * Gets the connection string.
	 *
	 * @return the connection string
	 */
	public String getConnectionString() {
		return connectionString;
	}

	/**
	 * Sets the connection string.
	 *
	 * @param connectionString the new connection string
	 */
	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	/**
	 * Gets the hdfscomands.
	 *
	 * @return the hdfscomands
	 */
	public HDFSLogic getHdfscomands() {
		return hdfscomands;
	}

	/**
	 * Sets the hdfscomands.
	 *
	 * @param hdfscomands the new hdfscomands
	 */
	public void setHdfscomands(HDFSLogic hdfscomands) {
		this.hdfscomands = hdfscomands;
	}

	/**
	 * Gets the yarn.
	 *
	 * @return the yarn
	 */
	public Yarn getYarn() {
		return yarn;
	}

	/**
	 * Sets the yarn.
	 *
	 * @param yarn the new yarn
	 */
	public void setYarn(Yarn yarn) {
		this.yarn = yarn;
	}

	/**
	 * Gets the folder input path.
	 *
	 * @return the folder input path
	 */
	public String getFolderInputPath() {
		return folderInputPath;
	}

	/**
	 * Sets the folder input path.
	 *
	 * @param folderInputPath the new folder input path
	 */
	public void setFolderInputPath(String folderInputPath) {
		this.folderInputPath = folderInputPath;
	}

	/**
	 * Gets the folder outpath.
	 *
	 * @return the folder outpath
	 */
	public String getFolderOutpath() {
		return folderOutpath;
	}

	/**
	 * Sets the folder outpath.
	 *
	 * @param folderOutpath the new folder outpath
	 */
	public void setFolderOutpath(String folderOutpath) {
		this.folderOutpath = folderOutpath;
	}
	
	/**
	 * Sets the yarn jar file to execute.
	 *
	 * @param Path the new yarn jar file to execute
	 */
	public void setYarnJarFileToExecute(String Path) {
		getYarn().setJarFileToExecute(Path);
	}
	
	/**
	 * Sets the yarn jar option.
	 *
	 * @param option the new yarn jar option
	 */
	public void setYarnJarOption(String option) {
		getYarn().setJarOption(option);
	}

	@Override
	public void downloadResult() {
		try {
			//System.out.println( "remotepath " + getOutputPath() + "  localpath "  + getFilePathToAnalize()  );
			getHdfscomands().copyToLocal(getOutputPath(), getFilePathToAnalize());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the result file.
	 *
	 * @return the result file
	 */
	public String getResultFile() {
		//System.out.println("directorio a hacer el ls  " + getFilePathToAnalize()+outputLocalFolder());
		try {
			Process auxProces;
			 auxProces = Runtime.getRuntime().exec("ls " + getFilePathToAnalize() + outputLocalFolder());
			BufferedReader reader =  new BufferedReader(new InputStreamReader(auxProces.getInputStream()));
			String line = "";           
			Integer i = 0;
		        while ((line = reader.readLine())!= null) {
		        	i++;
		        	//System.out.println("contenido del directorio "  + line + " numero de veces en el bucle  " + i  );
		        	//System.out.println("boolean devolviendo de la exprecion !line.contains(PDF) " +!line.toUpperCase().contains(PDF));
		        	if( (!line.contains(SUCCES)) && !line.toUpperCase().contains(PDF)) {
		        		return  ROOT+line;
		        	}
		        }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "null";
	}

	/**
	 * Outputlocal folder.
	 *
	 * @return the string
	 */
	@Override
	public String outputLocalFolder() {
		String [] auxFolder;
		auxFolder =  getOutputPath().split("/");
		return auxFolder[auxFolder.length - ONE];
	}

	@Override
	public boolean parseInputsFile(String path) {
		
		
		
		
		try {
			File auxInput = new File(path);
			Scanner input = new Scanner(new File(path));
			if(auxInput.exists()) {
				setConnectionString(input.nextLine()); // connnection stirng
				try {
					setHdfscomands(new HDFSLogic(getConnectionString()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				getYarn().setYarnHome(input.nextLine()); // ubicacion local del yarn
				setRemotePath(input.nextLine()); // path remoto para subir la informacion
				setOutputPath(input.nextLine()); // folder de salida
				getYarn().setJarFileToExecute(input.nextLine()); // ubicacion del jar file a ejecutar
				getYarn().setJarOption(input.nextLine()); // opciones del fichero jar a ejecutar
				if(input.hasNext()) { // opcional si solo se quiere analizar
					setFilePathToAnalize(input.nextLine()); // path donde esta el fichero a analizar
					setFileName(input.nextLine()); // nombre del fichero a analizar
				}
			/*	connection String HDFS
			 * 	hadoopYarn.getYarn().setYarnHome("/home/hadoop/hadoop-2.8.5/bin/yarn"); // a??adimos el home del yarn en local
				hadoopYarn.setRemotePath("/albertoHome/"); // a??adimos el path remoto donde se va a subir el fichero
				hadoopYarn.setOutputPath("/albertoHome/outputTEST1"); // a??adimos el folder para el fichero de salida
				hadoopYarn.setYarnJarFileToExecute("/home/hadoop/hadoop-2.8.5/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.8.5.jar"); // a??adimos el fichero jar a ejecuta
				hadoopYarn.setYarnJarOption("wordcount"); // a??adimos la opcion para el fichero jar
				opcional directorio donde cojer los datos
				opcional fichero de datos.
				*/
				
				
				
				
				
			}
			else {
				System.out.println(" el ficheor no existe " + path  );
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
}
