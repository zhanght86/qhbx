package com.rh.update.utils;

public class UpdateResultElement {
    private String m_itemName = "";

    private String m_itemResult = "";

    private String m_operationName = "";

    public UpdateResultElement() {
    }

    public String getItemName() {
        return m_itemName;
    }

    public String getItemResult() {
        return m_itemResult;
    }

    public String getOperationName() {
        return m_operationName;
    }

    public void setItemName(String itemName) {
        this.m_itemName = itemName;
    }

    public void setItemResult(String itemResult) {
        this.m_itemResult = itemResult;
    }

    public void setOperationName(String name) {
        m_operationName = name;
    }

    public String toString() {
        return this.m_itemName + " [" + this.m_operationName + "] ["
                + this.m_itemResult + "]";
    }

}
