package com.rh.core.comm.cms.mgr;

/**
 * @author liwei
 * 
 */
public class ExtendsField {

    private String idField = null;
    /** parent name field name (opetion) */
    private String nameField = null;
    /** is extends field name */
    private String extendsField = null;
    /** parent id field name */
    private String parentIdField = null;
    /** parent name field name (opetion) */
    private String parentNameField = null;

    /**
     * get id field
     * @return id field name
     */
    public String getIdField() {
        return idField;
    }

    /**
     * set id field name
     * @param idField - id field name
     */
    public void setIdField(String idField) {
        this.idField = idField;
    }

    /**
     * get name field's name
     * @return str
     */
    public String getNameField() {
        return nameField;
    }

    /**
     * set name fiedld
     * @param nameField - name field
     */
    public void setNameField(String nameField) {
        this.nameField = nameField;
    }

    /**
     * get extends field
     * @return field name
     */
    public String getExtendsField() {
        return extendsField;
    }

    /**
     * set extends file
     * @param extendsField - field name
     */
    public void setExtendsField(String extendsField) {
        this.extendsField = extendsField;
    }

    /**
     * get parent id field
     * @return field name
     */
    public String getParentIdField() {
        return parentIdField;
    }

    /**
     * set parent id field
     * @param parentIdField - field name
     */
    public void setParentIdField(String parentIdField) {
        this.parentIdField = parentIdField;
    }

    /**
     * get parent name field
     * @return - field name
     */
    public String getParentNameField() {
        return parentNameField;
    }

    /**
     * set parent name field
     * @param parentNameField - field name
     */
    public void setParentNameField(String parentNameField) {
        this.parentNameField = parentNameField;
    }

}
