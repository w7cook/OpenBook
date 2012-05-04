package models;

import java.util.*;
import javax.persistence.*;

import controllers.Secure;

import controllers.Comments;
import controllers.Secure;
import play.db.jpa.*;
import play.data.validation.*;


import play.libs.WS;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;



@Entity
@Table(uniqueConstraints = @UniqueConstraint(
		columnNames={"SUBSCRIBER", "URL"}))
public class RSSFeed extends Model {

	@Required
	@ManyToOne
	@JoinColumn(name="SUBSCRIBER")
	public User subscriber;
	@Required
	public String url;
	@Required
	public boolean is_valid;

	public RSSFeed(User subscriber, String url) {
		this.subscriber = subscriber;
		this.url = url;
		this.is_valid = this.validateURL();
	}


	public List<RSSFeedNode> getNodesByName(String name) {
		List<RSSFeedNode> list = new ArrayList<RSSFeedNode>();

		Document xml = getXML();
		if(xml != null){
			NodeList nodes = xml.getElementsByTagName(name);
			for(int i=0; i<=nodes.getLength(); i++) {
				if (nodes.item(i) == null)
					continue;
				list.add(new RSSFeedNode(nodes.item(i)));
			}

		}
		return list;
	}

	public RSSFeedNode getFirstNodeByName(String name) {
		if(getNodesByName(name).size()>0)
			return getNodesByName(name).get(0);
		return null;
	}

	public List<RSSFeedNode> getItems() {
		return getNodesByName("item");
	}

	public List<RSSFeedNode> getItems(int num) {
		List<RSSFeedNode> list = getItems();
		return (list.size() < num ? list : list.subList(0, num));
	}

	public String getTitle() {
		if(getFirstNodeByName("channel")!=null)
			return getFirstNodeByName("channel").ccontent("title");
		return null;
	}

	public String getChannelLink() {
		if(getFirstNodeByName("channel")!=null)
			return getFirstNodeByName("channel").ccontent("link");
		return null;
	}

	public RSSFeedNode getImageNode() {
		return getFirstNodeByName("image");
	}
	public String getImageNodeURL() {
		if(getFirstNodeByName("image") != null)
			return getFirstNodeByName("image").ccontent("url");
		return "";
	}
	public String getImageNodeWidth() {
		if(getFirstNodeByName("image") != null)
			return getFirstNodeByName("image").ccontent("width");
		return "";
	}
	public String getImageNodeHeight() {
		if(getFirstNodeByName("image") != null)
			return getFirstNodeByName("image").ccontent("height");
		return "";
	}

	public Document getXML() {
		try {
			Document xml = WS.url(this.url).get().getXml();
			if (!this.is_valid) {
				this.is_valid = validXML(xml);
				this.save();
			} 
			return xml;
		} catch(Exception e) {
			return null;
		}
	}

	public boolean validateURL() {
		try {
			Document xml = WS.url(this.url).get().getXml();
			return validXML(xml);
		} catch(Exception e) {
			return false;
		}
	}

	private boolean validXML(Document xml) {
		try {
			Node rss_node = xml.getElementsByTagName("rss").item(0);

			NamedNodeMap attribs = rss_node.getAttributes();
			Node version_node = attribs.getNamedItem("version");
			String version = version_node.getTextContent();

			return version.startsWith("2.");
		} catch(Exception e) {
			return false;
		}
	}

	public class RSSFeedNode {
		private Node n;

		RSSFeedNode(Node n) {
			this.n = n.cloneNode(true);
		}

		public String attr(String attr_name) {
			NamedNodeMap attribs = this.n.getAttributes();
			Node attrib_node = attribs.getNamedItem(attr_name);
			if (attrib_node == null) {
				return null;
			} 
			return attrib_node.getTextContent();
		}

		public void traverse() {
			traverse(this.n, 0);  
		}

		// For debuggin'
		private void traverse(Node n, int indent) {
			for(int i=0; i<indent; i++) {
				System.out.print(" "); 
			}
			String val = n.getNodeValue();
			if (val != null && val.length() > 15) {
				val = val.substring(0, 15);
			}
			System.out.println(n.getNodeName()+" ["+(val==null?null:val.trim())+"]");

			NodeList children = n.getChildNodes();
			for (int i=0; i<children.getLength(); i++) {
				Node n_n = children.item(i);
				if (n_n == null) continue;
				traverse(n_n, indent+1);
			}
		}

		/*
         Gets the named Child node string content
		 */
		public String ccontent(String child_name) {
			String rtn_content = "";

			NodeList children = this.n.getChildNodes();

			List<Node> relevant_nodes = new ArrayList<Node>();
			for(int i=0; i<children.getLength(); i++) {
				if (children.item(i).getNodeName().equalsIgnoreCase(child_name)) {
					relevant_nodes.add(children.item(i));
				}
			}

			for(Node n : relevant_nodes) {
				NodeList n_children = n.getChildNodes();
				for(int i=0; i<n_children.getLength(); i++) {
					Node n_child = n_children.item(i);
					if (n_child.getNodeType() == Node.TEXT_NODE) {
						String val = n_child.getNodeValue().trim();
						rtn_content += val;
					}
				}
			}

			return rtn_content;
		}
	}
}



