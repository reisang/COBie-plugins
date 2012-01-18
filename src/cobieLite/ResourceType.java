/*
 * XML Type:  ResourceType
 * Namespace: cobieLite
 * Java type: cobieLite.ResourceType
 *
 * Automatically generated - do not modify.
 */
package cobieLite;


/**
 * An XML ResourceType(@cobieLite).
 *
 * This is a complex type.
 */
public interface ResourceType extends cobieLite.COBIEBaseType
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ResourceType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s3A84CE08177E9EC2022DBEA3D9A338ED").resolveHandle("resourcetype5cd1type");
    
    /**
     * Gets the "CreatedBy" element
     */
    java.lang.String getCreatedBy();
    
    /**
     * Gets (as xml) the "CreatedBy" element
     */
    cobieLite.EmailAddressType xgetCreatedBy();
    
    /**
     * Sets the "CreatedBy" element
     */
    void setCreatedBy(java.lang.String createdBy);
    
    /**
     * Sets (as xml) the "CreatedBy" element
     */
    void xsetCreatedBy(cobieLite.EmailAddressType createdBy);
    
    /**
     * Gets the "CreatedOn" element
     */
    java.util.Calendar getCreatedOn();
    
    /**
     * Gets (as xml) the "CreatedOn" element
     */
    org.apache.xmlbeans.XmlDateTime xgetCreatedOn();
    
    /**
     * Sets the "CreatedOn" element
     */
    void setCreatedOn(java.util.Calendar createdOn);
    
    /**
     * Sets (as xml) the "CreatedOn" element
     */
    void xsetCreatedOn(org.apache.xmlbeans.XmlDateTime createdOn);
    
    /**
     * Gets the "Category" element
     */
    java.lang.String getCategory();
    
    /**
     * Gets (as xml) the "Category" element
     */
    org.apache.xmlbeans.XmlString xgetCategory();
    
    /**
     * Sets the "Category" element
     */
    void setCategory(java.lang.String category);
    
    /**
     * Sets (as xml) the "Category" element
     */
    void xsetCategory(org.apache.xmlbeans.XmlString category);
    
    /**
     * Gets the "ExtSystem" element
     */
    java.lang.String getExtSystem();
    
    /**
     * Gets (as xml) the "ExtSystem" element
     */
    org.apache.xmlbeans.XmlString xgetExtSystem();
    
    /**
     * Sets the "ExtSystem" element
     */
    void setExtSystem(java.lang.String extSystem);
    
    /**
     * Sets (as xml) the "ExtSystem" element
     */
    void xsetExtSystem(org.apache.xmlbeans.XmlString extSystem);
    
    /**
     * Gets the "ExtObject" element
     */
    java.lang.String getExtObject();
    
    /**
     * Gets (as xml) the "ExtObject" element
     */
    org.apache.xmlbeans.XmlString xgetExtObject();
    
    /**
     * Sets the "ExtObject" element
     */
    void setExtObject(java.lang.String extObject);
    
    /**
     * Sets (as xml) the "ExtObject" element
     */
    void xsetExtObject(org.apache.xmlbeans.XmlString extObject);
    
    /**
     * Gets the "ExtIdentifier" element
     */
    java.lang.String getExtIdentifier();
    
    /**
     * Gets (as xml) the "ExtIdentifier" element
     */
    org.apache.xmlbeans.XmlString xgetExtIdentifier();
    
    /**
     * Sets the "ExtIdentifier" element
     */
    void setExtIdentifier(java.lang.String extIdentifier);
    
    /**
     * Sets (as xml) the "ExtIdentifier" element
     */
    void xsetExtIdentifier(org.apache.xmlbeans.XmlString extIdentifier);
    
    /**
     * Gets the "Description" element
     */
    java.lang.String getDescription();
    
    /**
     * Gets (as xml) the "Description" element
     */
    org.apache.xmlbeans.XmlString xgetDescription();
    
    /**
     * Sets the "Description" element
     */
    void setDescription(java.lang.String description);
    
    /**
     * Sets (as xml) the "Description" element
     */
    void xsetDescription(org.apache.xmlbeans.XmlString description);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static cobieLite.ResourceType newInstance() {
          return (cobieLite.ResourceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static cobieLite.ResourceType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (cobieLite.ResourceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static cobieLite.ResourceType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (cobieLite.ResourceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static cobieLite.ResourceType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (cobieLite.ResourceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static cobieLite.ResourceType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (cobieLite.ResourceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static cobieLite.ResourceType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (cobieLite.ResourceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static cobieLite.ResourceType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (cobieLite.ResourceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static cobieLite.ResourceType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (cobieLite.ResourceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static cobieLite.ResourceType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (cobieLite.ResourceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static cobieLite.ResourceType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (cobieLite.ResourceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static cobieLite.ResourceType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (cobieLite.ResourceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static cobieLite.ResourceType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (cobieLite.ResourceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static cobieLite.ResourceType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (cobieLite.ResourceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static cobieLite.ResourceType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (cobieLite.ResourceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static cobieLite.ResourceType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (cobieLite.ResourceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static cobieLite.ResourceType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (cobieLite.ResourceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static cobieLite.ResourceType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (cobieLite.ResourceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static cobieLite.ResourceType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (cobieLite.ResourceType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
