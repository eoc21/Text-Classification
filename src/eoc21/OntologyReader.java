package eoc21;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class OntologyReader {
		private String ontologyURI;
		private OntModel ontologyModel;
		private Model rdfOntologyModel;
		/**
		 * 
		 * @param uri - Ontology uri.
		 */
		public OntologyReader(final String uri) {
			this.ontologyURI = uri;
		}
		/**
		 * Method to read in an ontology from a URI.
		 */
		public void readOntology() {
			ontologyModel = ModelFactory
					.createOntologyModel(OntModelSpec.OWL_MEM);
			setRDFModel(ontologyModel.read(ontologyURI, "RDF/XML"));
		}
		/**
		 * 
		 * @return OntModel - the ontology.
		 */
		public OntModel getOntologyModel() {
			return ontologyModel;
		}
		/**
		 * 
		 * @param propertiesOnt - RDF model for ChemAxiomProperties ontology.
		 */
		private void setRDFModel(final Model propertiesOnt) {
			this.rdfOntologyModel = propertiesOnt;
		}
		/**
		 * 
		 * @return RDFModel representation of the ontology.
		 */
		public Model getPropertiesOnt() {
			return rdfOntologyModel;
		}

}
