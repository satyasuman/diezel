package net.ericaro.diezel.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import net.ericaro.diezel._2_0.ObjectFactory;


public class FileUtils {

	public static void printFile(File file, String content, boolean close)
			throws IOException {
		file.getParentFile().mkdirs();
		//System.out.println("DIEZEL is generating "+file);
		OutputStreamWriter w = new OutputStreamWriter(
				new FileOutputStream(file));
		w.write(content);
		if (close)
			w.close();
	}


	
	/**
	 * 
	 * @param o
	 * @return
	 * @throws JAXBException
	 */
	public static JAXBContext getContext() throws JAXBException {
		return JAXBContext.newInstance(ObjectFactory.class);
	}

	/**
	 * Read one of the techno objects from a techno XML file.
	 * 
	 * @param file
	 *            a techno xml file
	 * @return
	 * @throws XMLStreamException
	 * @throws FactoryConfigurationError
	 * @throws JAXBException
	 * @throws IOException 
	 */
	public static Object read(String file) throws XMLStreamException, FactoryConfigurationError, JAXBException, IOException {
		return read(new File(file));
	}

	/**
	 * Read one of the techno objects from a techno XML file.
	 * 
	 * 
	 * 
	 * @param file
	 *            a techno File object
	 * @return
	 * @throws XMLStreamException
	 * @throws FactoryConfigurationError
	 * @throws JAXBException
	 * @throws IOException 
	 */
	public static Object read(File file) throws XMLStreamException, FactoryConfigurationError, JAXBException, IOException {
		FileInputStream in = new FileInputStream(file);
		try {
			return read(in);
		} finally {
			
			in.close();
			
		}
	}

	/**
	 * Read one of the techno objects from a techno XML file inputstream.
	 * 
	 * @param in
	 *            inputstream to a techno file content
	 * @return
	 * @throws XMLStreamException
	 * @throws FactoryConfigurationError
	 * @throws JAXBException
	 */
	public static Object read(InputStream in) throws XMLStreamException, FactoryConfigurationError, JAXBException {
		XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(in);
		return read(reader);
	}

	/**
	 * Read one of the techno objects from a techno XML reader. Warning this
	 * methods closes the reader.
	 * 
	 * @param source
	 *            an xmlstream reader
	 * @return
	 * @throws JAXBException
	 * @throws XMLStreamException
	 */
	public static Object read(XMLStreamReader source) throws JAXBException, XMLStreamException {
		Unmarshaller u = getContext().createUnmarshaller();
		return u.unmarshal(source);
	}

	/** Write any of the techno objects into a file
	 * 
	 * @param object one of the techno objects
	 * @param file a file path
	 * @throws JAXBException
	 * @throws IOException
	 */
	public static void write(Object object, String file) throws JAXBException, IOException {
		write(object, new File(file) );
	}
	
	/** Write any of the techno objects into a file
	 * 
	 * @param object one of the techno objects
	 * @param file a file to write to
	 * @throws JAXBException
	 * @throws IOException
	 */
	public static void write(Object object, File file) throws JAXBException, IOException {
		FileOutputStream out = new FileOutputStream(file);
		try {
			write(object, out);
		} finally {
			out.close();
		}
	}

	/** Write any of the techno objects into a stream
	 * 
	 * @param object
	 * @param out
	 * @throws JAXBException
	 */
	public static void write(Object object, OutputStream out) throws JAXBException {
		Marshaller m = getContext().createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(object, out);
	}

	/** 
	 * @return an URL for the schema location. 
	 */
	public static URL getSchemaURL() {
		return FileUtils.class.getResource("/Diezel.xsd");
	}


	
}
