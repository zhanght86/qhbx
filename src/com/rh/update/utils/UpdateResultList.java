package com.rh.update.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class UpdateResultList {
    private ArrayList m_list = new ArrayList();

    public UpdateResultList() {
    }

    public void addElement(UpdateResultElement element) {
        if (element != null) {
            m_list.add(element);
        }
    }

    public UpdateResultElement getElement(int index) {
        if (index < m_list.size()) {
            return (UpdateResultElement) m_list.get(index);
        }
        return null;
    }

    public int size() {
        return m_list.size();
    }
    
    public void addElementList(UpdateResultList list){
        int size = list.size();
        for(int i=0;i < size;i++){
            this.m_list.add(list.getElement(i));
        }
    }
    
    public void writeToFile(File file)throws FileNotFoundException,IOException{
        FileOutputStream output = null;
        try{
            output = new FileOutputStream(file);
            for(int i=0;i<m_list.size();i++){
                UpdateResultElement element = (UpdateResultElement)m_list.get(i);
                output.write(new String(element.toString() + "\r\n").getBytes());
            }
        }finally{
            if(output!=null){
                output.close() ;
            }
        }
        
    }
}
