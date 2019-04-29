package terms_extractor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.ocl.OCL;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.Query;
import org.eclipse.ocl.ecore.Constraint;
import org.eclipse.ocl.ecore.EcoreEnvironmentFactory;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.helper.OCLHelper;

public class Extractor {

	public static void main(String[] args) throws IOException, ParserException {

		try (Stream<Path> paths = Files.walk(Paths.get("metamodel/"))) {
			Map<Path,String> textMap = new HashMap<Path, String>();
			paths.filter(Files::isRegularFile).forEach(
					z -> textMap.put(z, extractBigrams(z.toString())));
			for (Entry<Path,String> entry : textMap.entrySet()) {
				Path path = Paths.get(entry.getKey().toString().replace("metamodel/", "bigrams/").replace(".ecore", ".txt"));
				try (BufferedWriter writer = Files.newBufferedWriter(path))
				{
				    writer.write(entry.getValue());
				}
			}
		} catch (IOException e) {

		}
		extractUnigram("metamodel/Business.ecore");
		
	}

	public static String getMetricAs(String string) throws ParserException {
		EcorePackage.eINSTANCE.eClass();

		// Register the XMI resource factory for the .website extension

		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("ecore", new XMIResourceFactoryImpl());

		// Obtain a new resource set
		ResourceSet resSet = new ResourceSetImpl();

		// Get the resource
		Resource resource = resSet.getResource(URI.createURI(string), true);
		// Get the first model element and cast it to the right type, in my
		// example everything is hierarchical included in this first node
		EPackage myWeb = (EPackage) resource.getContents().get(0);

		int a = getNumMC(myWeb);
		int b = getNumAttr(myWeb);
		int c = getNumRef(myWeb);
		return (a + "," + b + "," + c + "\n");
	}

	public static String extractUnigram(String path) {
		StringJoiner builder = new StringJoiner(" ");
		URI fileURI = URI.createFileURI(path);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		ResourceSet resourceSet = new ResourceSetImpl();
		try {
			Resource resource = resourceSet.getResource(fileURI, true);
			if (resource.isLoaded() && resource.getErrors() != null) {
				TreeIterator<EObject> eAllContents = resource.getAllContents();
				while (eAllContents.hasNext()) {
					EObject next = eAllContents.next();
					if (next instanceof EPackage) {
						EPackage ePackage = (EPackage) next;
						builder.add(ePackage.getName());
					} else if (next instanceof EClass) {
						EClass eClass = (EClass) next;
						builder.add(eClass.getName());
					} else if (next instanceof EEnum) {
						EEnum eEnum = (EEnum) next;
						builder.add(eEnum.getName());
					} else if (next instanceof EDataType) {
						EDataType eDataType = (EDataType) next;
						builder.add(eDataType.getName());
					} else if (next instanceof EAnnotation) {
						// GET all the EAnnotations
						EList<EAnnotation> annotations = ((EModelElement) next).getEAnnotations();
						builder.add(annotations.toString());
					} else if (next instanceof EAttribute) {
						EAttribute eAttribute = (EAttribute) next;
						builder.add(eAttribute.getName());
					} else if (next instanceof EReference) {
						EReference eReference = (EReference) next;
						builder.add(eReference.getName());
					}
				}
			}
			return builder.toString();

		} catch (Exception e) {
			System.err.println("ERROR: " + path + " " + e.getMessage());
			return "";
		}
	}

	public static String extractBigrams(String path) {
		StringJoiner builder = new StringJoiner(" ");
		URI fileURI = URI.createFileURI(path);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		ResourceSet resourceSet = new ResourceSetImpl();
		try {
			Resource resource = resourceSet.getResource(fileURI, true);
			if (resource.isLoaded() && resource.getErrors() != null) {
				TreeIterator<EObject> eAllContents = resource.getAllContents();
				while (eAllContents.hasNext()) {
					EObject next = eAllContents.next();
//					if (next instanceof EPackage) {
//						EPackage ePackage = (EPackage) next;
//						builder.add(ePackage.getName());
//					} else 
					if (next instanceof EClass) {
						EClass eClass = (EClass) next;
						builder.add(eClass.getEPackage().getName() + "." + eClass.getName());
					} else if (next instanceof EEnum) {
						EEnum eEnum = (EEnum) next;
						builder.add(eEnum.getEPackage().getName() + "." + eEnum.getName());
					}
//					else if (next instanceof EDataType) {
//						EDataType eDataType = (EDataType) next;
//						builder.add(eDataType.getEPackage() + "." + eDataType.getName());
//					} 
					else if (next instanceof EAttribute) {
						EAttribute eAttribute = (EAttribute) next;
						try {
							builder.add(((EClass) eAttribute.eContainer()).getName() + "." + eAttribute.getName());
						} catch (Exception e) {
						}
					} else if (next instanceof EReference) {
						EReference eReference = (EReference) next;
						try {
							builder.add(((EClass) eReference.eContainer()).getName() + "." + eReference.getName());
						} catch (Exception e) {
						}
					}
				}
			}
			return builder.toString();

		} catch (Exception e) {
			System.err.println("ERROR: " + path + " " + e.getMessage());
			return "";
		}
	}

	public static String extractNgrams(String path) {
		StringJoiner builder = new StringJoiner(" ");
		URI fileURI = URI.createFileURI(path);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		ResourceSet resourceSet = new ResourceSetImpl();
		try {
			Resource resource = resourceSet.getResource(fileURI, true);
			if (resource.isLoaded() && resource.getErrors() != null) {
				TreeIterator<EObject> eAllContents = resource.getAllContents();
				while (eAllContents.hasNext()) {
					EObject next = eAllContents.next();
					if (next instanceof EPackage) {
						EPackage ePackage = (EPackage) next;
						builder.add(ePackage.getName());
					} else if (next instanceof EClass) {
						EClass eClass = (EClass) next;
						builder.add(
								eClass.getEPackage().getName() + "." + eClass.getName() + "." + eClass.isAbstract());
						for (EClass iterable_element : eClass.getESuperTypes()) {
							builder.add(iterable_element.getName() + "." + eClass.getName());
						}
					} else if (next instanceof EEnum) {
						EEnum eEnum = (EEnum) next;
						builder.add(eEnum.getEPackage().getName() + "." + eEnum.getName());
						for (EEnumLiteral literal : eEnum.getELiterals()) {
							builder.add(eEnum.getName() + "." + literal.getName());
						}
					} else if (next instanceof EDataType) {
						EDataType eDataType = (EDataType) next;
						builder.add(eDataType.getEPackage().getName() + "." + eDataType.getName());
					} else if (next instanceof EAttribute) {
						EAttribute eAttribute = (EAttribute) next;
						if (eAttribute.getEType() != null)
							builder.add(((EClass) eAttribute.eContainer()).getName() + "." + eAttribute.getName() + "."
									+ eAttribute.getEType().getName() + "." + eAttribute.getLowerBound() + "."
									+ eAttribute.getUpperBound());
						else
							builder.add(((EClass) eAttribute.eContainer()).getName() + "." + eAttribute.getName() + "."
									+ eAttribute.getLowerBound() + "." + eAttribute.getUpperBound());
					} else if (next instanceof EReference) {
						EReference eReference = (EReference) next;
						if (eReference.getEType() != null)
							builder.add(((EClass) eReference.eContainer()).getName() + "." + eReference.getName() + "."
									+ eReference.getEType().getName() + "." + eReference.isContainer() + "."
									+ eReference.getLowerBound() + "." + eReference.getUpperBound());
						else
							builder.add(((EClass) eReference.eContainer()).getName() + "." + eReference.getName() + "."
									+ "." + eReference.isContainer() + "." + eReference.getLowerBound() + "."
									+ eReference.getUpperBound());
					}
				}
			}
			return builder.toString();

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("ERROR: " + path + " " + e.getMessage());
			return "";
		}
	}

	public static int getNumMC(EPackage atlModel) throws ParserException {
		OCL<?, EClassifier, ?, ?, ?, EParameter, ?, ?, ?, Constraint, EClass, EObject> ocl;
		OCLHelper<EClassifier, ?, ?, Constraint> helper;
		ocl = OCL.newInstance(EcoreEnvironmentFactory.INSTANCE);
		helper = ocl.createOCLHelper();
		helper.setContext(EcorePackage.eINSTANCE.getEPackage());
		OCLExpression<EClassifier> expression = helper.createQuery("EClass.allInstances()");
		Query<EClassifier, EClass, EObject> query = ocl.createQuery(expression);
		HashSet<Object> success = (HashSet<Object>) query.evaluate(atlModel);
		return success.size();
	}

	public static int getNumAttr(EPackage atlModel) throws ParserException {
		OCL<?, EClassifier, ?, ?, ?, EParameter, ?, ?, ?, Constraint, EClass, EObject> ocl;
		OCLHelper<EClassifier, ?, ?, Constraint> helper;
		ocl = OCL.newInstance(EcoreEnvironmentFactory.INSTANCE);
		helper = ocl.createOCLHelper();
		helper.setContext(EcorePackage.eINSTANCE.getEPackage());
		OCLExpression<EClassifier> expression = helper.createQuery("EAttribute.allInstances()");
		Query<EClassifier, EClass, EObject> query = ocl.createQuery(expression);
		HashSet<Object> success = (HashSet<Object>) query.evaluate(atlModel);
		return success.size();
	}

	public static int getNumRef(EPackage atlModel) throws ParserException {
		OCL<?, EClassifier, ?, ?, ?, EParameter, ?, ?, ?, Constraint, EClass, EObject> ocl;
		OCLHelper<EClassifier, ?, ?, Constraint> helper;
		ocl = OCL.newInstance(EcoreEnvironmentFactory.INSTANCE);
		helper = ocl.createOCLHelper();
		helper.setContext(EcorePackage.eINSTANCE.getEPackage());
		OCLExpression<EClassifier> expression = helper.createQuery("EReference.allInstances()");
		Query<EClassifier, EClass, EObject> query = ocl.createQuery(expression);
		HashSet<Object> success = (HashSet<Object>) query.evaluate(atlModel);
		return success.size();
	}
}
