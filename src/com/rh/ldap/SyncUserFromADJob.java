package com.rh.ldap;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.rh.core.base.Bean;
import com.rh.core.comm.schedule.job.RhJob;
import com.rh.core.serv.ServDao;


public class SyncUserFromADJob implements RhJob {
    
    private static Log log = LogFactory.getLog(SyncUserFromADJob.class);
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LdapContext ctx = null;
        ADMgr admgr = new ADMgr();
        AD ad= admgr.getAD();
        try {
            ctx = ad.getConn();
            NamingEnumeration<SearchResult>  list = null;
//                  list = ad.search("OU=前海再保险,DC=qianhaire,DC=local", "objectClass=User", new String[] { "objectGUID",
//                            "sAMAccountName", "name", "distinguishedName", "mail", "title", "whenCreated", "whenChanged" });
//                SearchControls sdf = new SearchControls();
//                sdf.setReturningAttributes(new String[] {
//                       "name"});
//                      list = ad.search("DC=qianhaire,DC=local",
//                            "objectClass=User",sdf );
//                    System.out.println(list);
//                    while(list.hasMoreElements()){
//                        SearchResult sr = list.nextElement();
//                        System.out.println(sr.getAttributes());
//                    }
                    
                    SearchControls searchCtls = new SearchControls();  
                    searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);  
                    String addTime =  "20161229000000.0Z";
                    String searchFilter = "(&(objectCategory=person)(objectClass=user)(name=*)(whenChanged>="+
                            addTime
                            +"))";  
                    String searchBase = "OU=前海再保险,DC=qianhaire,DC=local";  
                    String returnedAtts[] = {"memberOf","whenChanged","whenCreated","name","title","mail",
                          "sAMAccountName", "name", "distinguishedName", "telephoneNumber", "homePhone",  
                           "mobile"};  
                    searchCtls.setReturningAttributes(returnedAtts);  
                    NamingEnumeration<SearchResult> answer = ctx.search(searchBase, searchFilter,searchCtls);  
                    List<Bean> aBeanList = new ArrayList<Bean>();
                    while (answer.hasMoreElements()) {  
                        SearchResult sr = (SearchResult) answer.next();  
                        System.out.println( "username ："+sr.getName()+",attributes:"+sr.getAttributes()+",samaccountname:"+sr.getAttributes().get("samaccountname").getID());  
                        String loginName = "",name ="",mail="",officePhone="",mobile="",telephonenumber="";
                        if(sr.getAttributes().get("samaccountname").toString().contains(":")){
                                loginName = sr.getAttributes().get("samaccountname").toString().substring(sr.getAttributes().get("samaccountname").toString().indexOf(":")+1);
                                name = sr.getName().substring(sr.getName().indexOf("CN="),sr.getName().indexOf(","));
                                if(sr.getAttributes().get("telephonenumber").toString().length()>0){
                                    telephonenumber = sr.getAttributes().get("telephonenumber").toString().substring(sr.getAttributes().get("telephonenumber").toString().indexOf(":"));
                                }
                                if(sr.getAttributes().get("mobile").toString().length()>0){
                                    mobile = sr.getAttributes().get("mobile").toString().substring(sr.getAttributes().get("mobile").toString().indexOf(":"));
                                }
                                if(sr.getAttributes().get("mail").toString().length()>0){
                                    mail = sr.getAttributes().get("mail").toString().substring(sr.getAttributes().get("mail").toString().indexOf(":"));
                                }
                                Bean uBean = new Bean();
                                List<Bean>  userBeanList = ServDao.finds("SY_ORG_USER", " and USER_LOGIN_NAME = "+loginName+"'");
                                if(userBeanList.size()==0){
                                    uBean.set("USER_NAME", name);
                                    uBean.set("USER_OFFICE_PHONE", telephonenumber);
                                    uBean.set("USER_MOBILE", mobile);
                                    uBean.set("USER_MAIL", mail);
                                    aBeanList.add(uBean);
                                    log.info("同步域用户："+sr.getAttributes()+"准备添加成功。");
                                }else if(userBeanList.size()==1){
                                    uBean = userBeanList.get(0);
                                    uBean.set("USER_NAME", name);
                                    uBean.set("USER_OFFICE_PHONE", telephonenumber);
                                    uBean.set("USER_MOBILE", mobile);
                                    uBean.set("USER_MAIL", mail);
                                    ServDao.update("SY_ORG_USER", uBean);
                                    log.info("同步域用户成功："+sr.getAttributes()+"修改成功。");
                                }else{
                                    //记录失败信息
                                    log.error("同步域用户信息失败【"+loginName+"】在OA系统中不唯一");
                                }
                        }else{//记录失败的信息
                            //记录失败信息
                            log.error("获取域用户信息登录名失败，【"+ "username ："+sr.getName()+",attributes:"+sr.getAttributes()+",samaccountname:"+sr.getAttributes().get("samaccountname").getID()+"】在域系统中没有获取到");
                        }
                       
                    } 
                    if(aBeanList.size()>0){
                    ServDao.creates("SY_ORG_USER", aBeanList);
                    log.info("同步域用户成功："+aBeanList.size()+"个用户添加成功。");
                    }
        } catch (Exception e) {
            log.error("同步域用户失败：", e);
        } finally {
            ad.colseConn(ctx);
        }
    }
    
    @Override
    public void interrupt() {
        // TODO Auto-generated method stub
        
    }
    
}
