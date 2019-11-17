package workflow.executioner;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import transitions.ConditionalTransition;
import transitions.ParallelTransition;
import transitions.SimpleTransition;
import transitions.TerminalTransition;
import transitions.Transition;

public class WorkFlowManager {
	final String pathOfJarFile;
	// <name of the state,Transition type>
	final private LinkedHashMap<String, Transition> transitions = new LinkedHashMap<String, Transition>();
	// <name of the class,loaded class>
	final private LinkedHashMap<String, Class<?>> stateMap = new LinkedHashMap<>();
	private Object commonResource;
	private Class<?> resourceClass;

	public WorkFlowManager(String pathOfJarFile) {
		this.pathOfJarFile = pathOfJarFile;
	}

	/**
	 * Loading classes from jar file -> computing the
	 * stateMap<nameOfTheClas,Class<?>
	 * 
	 * @throws Exception
	 */
	private void loadClassesFromJar() throws Exception {
		JarFile jarFile = new JarFile(pathOfJarFile);
		Enumeration<JarEntry> entries = jarFile.entries();
		URL[] urls = { new URL("jar:file:" + pathOfJarFile + "!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);

		while (entries.hasMoreElements()) {
			JarEntry jarEntry = entries.nextElement();
			if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
				continue;
			}
			// Removing .class from className
			String className = jarEntry.getName().substring(0, jarEntry.getName().length() - 6);
			Class<?> c = cl.loadClass(className);
			// TODO:Check that every class implements Runnable
			stateMap.put(className, c);
		}
		jarFile.close();
	}

	/**
	 * Constructing transitions map ->transitions<NameOfTheState,Transition>
	 * 
	 * @param XMLFileName
	 */
	private void readTransitionsFromXML(String XMLFileName) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(XMLFileName);
			NodeList transitionList = doc.getElementsByTagName("transition");
			for (int i = 0; i < transitionList.getLength(); i++) {
				Node p = transitionList.item(i);
				if (p.getNodeType() == Node.ELEMENT_NODE) {
					Element transition = (Element) p;
					String attribute = transition.getAttribute("attribute");

					if (attribute.equals("simple")) {
						String s1 = "", s2 = "";
						NodeList sList = transition.getChildNodes();
						for (int j = 0; j < sList.getLength(); j++) {
							Node n = sList.item(j);
							if (n.getNodeType() == Node.ELEMENT_NODE) {
								Element e = (Element) n;
								if (e.getTagName().equals("s1")) {
									s1 = e.getTextContent();
								}
								if (e.getTagName().equals("s2")) {
									s2 = e.getTextContent();
								}
							}
						}
						transitions.put(stateMap.get(s1).getSimpleName(), new SimpleTransition(
								stateMap.get(s1).getSimpleName(), stateMap.get(s2).getSimpleName()));
					}

					if (attribute.equals("conditioned")) {
						String s1 = "", s2 = "", s3 = "", bool2 = "", bool3 = "";
						// bool2 este booleanul ca s2 sa se intample , bool3 e booleanul ca s3 sa se
						// intample
						// ex: daca bool2 e true atunci se intampla s2 , daca nu , s2 nu se va intampla
						NodeList sList = transition.getChildNodes();
						for (int j = 0; j < sList.getLength(); j++) {
							Node n = sList.item(j);
							if (n.getNodeType() == Node.ELEMENT_NODE) {
								Element e = (Element) n;
								if (e.getTagName().equals("s1")) {
									s1 = e.getTextContent();
								}
								if (e.getTagName().equals("s2")) {
									s2 = e.getTextContent();
								}
								if (e.getTagName().equals("s3")) {
									s3 = e.getTextContent();
								}
								if (e.getTagName().equals("bool2")) {
									bool2 = e.getTextContent();
								}
								if (e.getTagName().equals("bool3")) {
									bool3 = e.getTextContent();
								}
							}
						}
						transitions.put(stateMap.get(s1).getSimpleName(),
								new ConditionalTransition(stateMap.get(s1).getSimpleName(),
										stateMap.get(s2).getSimpleName(), stateMap.get(s3).getSimpleName(),
										Boolean.parseBoolean(bool2), Boolean.parseBoolean(bool3)));

					}

					if (attribute.equals("paralel")) {
						String s1 = "", s2 = "", s3 = "";
						NodeList sList = transition.getChildNodes();
						for (int j = 0; j < sList.getLength(); j++) {
							Node n = sList.item(j);
							if (n.getNodeType() == Node.ELEMENT_NODE) {
								Element e = (Element) n;
								if (e.getTagName().equals("s1")) {
									s1 = e.getTextContent();
								}
								if (e.getTagName().equals("s2")) {
									s2 = e.getTextContent();
								}
								if (e.getTagName().equals("s3")) {
									s3 = e.getTextContent();
								}
							}
						}
						Transition paral = new ParallelTransition(stateMap.get(s1).getSimpleName());
						((ParallelTransition) paral).addState(stateMap.get(s2).getSimpleName());
						((ParallelTransition) paral).addState(stateMap.get(s3).getSimpleName());
						transitions.put(stateMap.get(s1).getSimpleName(), paral);
					}

					if (attribute.equals("terminal")) {
						String s1 = "";
						NodeList sList = transition.getChildNodes();
						for (int j = 0; j < sList.getLength(); j++) {
							Node n = sList.item(j);
							if (n.getNodeType() == Node.ELEMENT_NODE) {
								Element e = (Element) n;
								if (e.getTagName().equals("s1")) {
									s1 = e.getTextContent();
								}
							}
						}
						transitions.put(stateMap.get(s1).getSimpleName(),
								new TerminalTransition(stateMap.get(s1).getSimpleName()));
					}

				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Main function: ->loading transitions and classes ->loading the resource using
	 * reflection
	 * 
	 * @throws Exception
	 */
	public void initTransitionsAndResource() throws Exception {
		loadClassesFromJar();
		readTransitionsFromXML("ex.xml");

		/* Finding conditional transitions */
		Iterator<Transition> it = transitions.values().iterator();
		List<String> conditionalStates = new ArrayList<String>();
		while (it.hasNext()) {
			Transition transition = it.next();
			if (transition instanceof ConditionalTransition) {
				ConditionalTransition cond = (ConditionalTransition) transition;
				conditionalStates.add(cond.getStartState());
			}
		}
		// LOADING the resource
		resourceClass = stateMap.get("Resource");
		Constructor<?> constructor = resourceClass.getConstructor(conditionalStates.getClass().getInterfaces()[0]);
		commonResource = constructor.newInstance(conditionalStates);

		/* Starting the workflow */
		Iterator<String> iterator = transitions.keySet().iterator();
		if (iterator.hasNext()) {
			recursivePath(Arrays.asList(iterator.next()));
		}

	}

	/**
	 * Number of threads created is given by paths.size If paths.size == 1 one
	 * thread will run If paths.size > 1 starting parallel threads
	 * 
	 * @param paths = ClassNames ->implementing Runnable
	 * @throws Exception
	 */
	public void recursivePath(List<String> paths) throws Exception {
		if (paths.size() == 1) {
			Thread thread;
			Class<?> loadedClass = stateMap.get(paths.get(0));
			Constructor<?> constructorOfLoadedClass = loadedClass.getConstructor(resourceClass);
			Object newState = constructorOfLoadedClass.newInstance(commonResource);
			thread = new Thread((Runnable) newState);
			thread.start();
			thread.join();
			if (checkTransition(paths.get(0))) {
				return;
			}

		} else {
			Thread[] thread = new Thread[paths.size()];
			for (int i = 0; i < paths.size(); i++) {
				Class<?> loadedClass = stateMap.get(paths.get(i));
				Constructor<?> constructorOfLoadedClass = loadedClass.getConstructor(resourceClass);
				Object newState = constructorOfLoadedClass.newInstance(commonResource);
				thread[i] = new Thread((Runnable) newState);
				thread[i].start();
			}

			for (int i = 0; i < paths.size(); i++) {
				thread[i].join();
			}

			for (int i = 0; i < paths.size(); i++) {
				if (checkTransition(paths.get(i))) {
					return;
				}
			}

		}

	}

	/**
	 * Checking the corresponding type of the State
	 * 
	 * @param path is containing state Name
	 * @return if the transition is terminal the the recursive call is stoped
	 * @throws Exception
	 */
	private boolean checkTransition(String path) throws Exception {
		Transition transition = transitions.get(path);
		if (transition.transitionType("SimpleTransition") == true) {
			SimpleTransition simpleTrans = (SimpleTransition) transition;
			recursivePath(Arrays.asList(simpleTrans.getFinishState()));
		} else if (transition.transitionType("ConditionalTransition") == true) {
			ConditionalTransition condTrans = (ConditionalTransition) transition;
			Method method = resourceClass.getMethod("getFlag", String.class);
			boolean flag = (boolean) method.invoke(commonResource, path);
			if (flag == condTrans.isFlagPathA()) {
				recursivePath(Arrays.asList(condTrans.getPathA()));
			} else {
				recursivePath(Arrays.asList(condTrans.getPathB()));
			}

		} else if (transition.transitionType("ParallelTransition") == true) {
			ParallelTransition parallelTrans = (ParallelTransition) transition;
			recursivePath(parallelTrans.getStates());
		} else if (transition.transitionType("TerminalTransition") == true) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		WorkFlowManager work = new WorkFlowManager("C:/Users/Victor/Desktop/someProject.jar");
		try {
			work.initTransitionsAndResource();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
