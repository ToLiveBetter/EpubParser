import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Content {

	private Metadata metadata;
	private Manifest manifest;
	private Spine spine;

	public Content() {
		metadata = new Metadata();
		manifest = new Manifest();
		spine = new Spine();
	}

	public class Metadata {
		private XmlItem rights;
		private XmlItem identifier;
		private XmlItem contributor;
		private XmlItem creator;
		private XmlItem title;
		private XmlItem language;
		private XmlItem subject;
		private XmlItem description;
		private XmlItem publisher;

		public XmlItem getRights() {
			return rights;
		}

		public XmlItem getIdentifier() {
			return identifier;
		}

		public XmlItem getContributor() {
			return contributor;
		}

		public XmlItem getCreator() {
			return creator;
		}

		public XmlItem getTitle() {
			return title;
		}

		public XmlItem getLanguage() {
			return language;
		}

		public XmlItem getSubject() {
			return subject;
		}

		public XmlItem getDescription() {
			return description;
		}

		public XmlItem getPublisher() {
			return publisher;
		}

		public void fillAttributes(NodeList nodeList)
				throws IllegalArgumentException, IllegalAccessException, DOMException {
			Field[] fields = Content.Metadata.class.getDeclaredFields();

			for (int i = 0; i < nodeList.getLength(); i++) {
				for (int j = 0; j < fields.length; j++) {
					if (nodeList.item(i).getNodeName().contains(fields[j].getName())) {
						fields[j].setAccessible(true);
						fields[j].set(this, nodeToXmlItem(nodeList.item(i)));
					}
				}
			}

		}

		public void printFields() throws IllegalArgumentException, IllegalAccessException {
			System.out.println("\n\nPrinting Metadata...\n");

			System.out.println("rights: " + (getRights() != null ? getRights().getValue() : null));
			System.out.println("identifier: " + (getIdentifier() != null ? getIdentifier().getValue() : null));
			System.out.println("contributor: " + (getContributor() != null ? getContributor().getValue() : null));
			System.out.println("creator: " + (getCreator() != null ? getCreator().getValue() : null));
			System.out.println("title: " + (getTitle() != null ? getTitle().getValue() : null));
			System.out.println("language: " + (getLanguage() != null ? getLanguage().getValue() : null));
			System.out.println("subject: " + (getSubject() != null ? getSubject().getValue() : null));
			System.out.println("description: " + (getDescription() != null ? getDescription().getValue() : null));
			System.out.println("publisher: " + (getPublisher() != null ? getPublisher().getValue() : null));
		}
	}

	public class Manifest {
		private List<XmlItem> xmlItemList;

		public Manifest() {
			this.xmlItemList = new ArrayList<>();
		}

		public void fillXmlItemList(NodeList nodeList) {
			this.xmlItemList = nodeListToXmlItemList(nodeList);
		}

		public void printXmlItems() {
			System.out.println("\n\nPrinting Manifest...\n");

			for (int i = 0; i < xmlItemList.size(); i++) {
				XmlItem xmlItem = xmlItemList.get(i);

				System.out.println("xmlItem(" + i + ")" + ": value:" + xmlItem.getValue() + " attributes: "
						+ xmlItem.getAttributes());
			}
		}
	}

	public class Spine {
		private List<XmlItem> xmlItemList; // <b>Ordered</b> Term of Contents, mostly filled with ids of application/xhtml+xml files in manifest node.

		public Spine() {
			this.xmlItemList = new ArrayList<>();
		}

		public void fillXmlItemList(NodeList nodeList) {
			this.xmlItemList = nodeListToXmlItemList(nodeList);
		}

	}

	public Metadata getMetadata() {
		return metadata;
	}

	public Manifest getManifest() {
		return manifest;
	}

	private List<XmlItem> nodeListToXmlItemList(NodeList nodeList) {

		List<XmlItem> xmlItemList = new ArrayList<>();

		for (int i = 0; i < nodeList.getLength(); i++) {
			XmlItem xmlItem = nodeToXmlItem(nodeList.item(i));
			if (!xmlItem.getValue().replaceAll("\\s+", "").equals("") || xmlItem.getAttributes() != null) {
				xmlItemList.add(xmlItem);
			}
		}

		return xmlItemList;
	}

	private XmlItem nodeToXmlItem(Node node) {
		XmlItem xmlItem = new XmlItem();
		xmlItem.setValue(node.getTextContent());

		if (node.hasAttributes()) {
			NamedNodeMap nodeMap = node.getAttributes();

			Map<String, String> attributes = new HashMap<>();

			for (int j = 0; j < nodeMap.getLength(); j++) {
				Node attribute = nodeMap.item(j);
				attributes.put(attribute.getNodeName(), attribute.getNodeValue());
			}

			xmlItem.setAttributes(attributes);
		}

		return xmlItem;
	}

}