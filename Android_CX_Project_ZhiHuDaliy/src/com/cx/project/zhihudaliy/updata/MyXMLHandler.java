package com.cx.project.zhihudaliy.updata;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * 用于XML解析
 * @author CxiaoX
 *
 * 2014年12月9日下午4:36:22
 */
public class MyXMLHandler extends DefaultHandler{
	Updata updata=null;
	
	private static final int TITLE = 1;
	private static final int MESSAGE = 2;
	private static final int VERSION = 3;
	private static final int URL = 4;
	
	private int currentIndex=0;
	
	
	/**
	 * 得到更新信息的对象。
	 * @return
	 */
	public Updata getUpdata(){
		return updata;
	}
	

	@Override
	public void startDocument() throws SAXException {
		
		updata = new Updata();
	}

	

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if("title".equals(qName)){
			currentIndex = TITLE ;
			
		}else if("message".equals(qName)){
			currentIndex = MESSAGE ;
		}else if("version".equals(qName)){
			currentIndex = VERSION;
		}else if("url".equals(qName)){
			currentIndex = URL;
		}
	
	}

	

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String value = new String(ch,start,length);
		switch (currentIndex) {
		case TITLE:
			updata.setTitle(value);
			break;
		case MESSAGE:
			updata.setDetail(value);
			break;
		case VERSION:
			updata.setVersion(Integer.parseInt(value));
			break;
		case URL:
			updata.setUrl(value);
			break;
		default:
			break;
		}
		
		currentIndex = 0;
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
	
		
	}
	
	
	@Override
	public void endDocument() throws SAXException {
	}
	

}
