package com.cisco.bccsp.util.ldap;



import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;

import com.cisco.bccsp.db.dao.checkUserPermissionConflict;
import com.cisco.bccsp.model.User;
import com.cisco.bccsp.model.groupMemberLDAP;

public class LDAPUtil {

    private static Logger logger = Logger.getLogger(LDAPUtil.class.getName());
	
	
	private static LDAPUtil ldaputil = null;
	private static Hashtable<String,String> ldapenv = null;
	private static String env = null;
	
	
	public LDAPUtil(String env) {
		
		ResourceBundle rb = null;
		
		if (env == null) {
			env = "dev";
		}
		
		this.env = env;
		
		rb = ResourceBundle.getBundle("com.cisco.bccsp.ldap.ldap_dev" );
		
        	
		ldapenv = new Hashtable<String,String>();
		
		ldapenv.put("com.sun.jndi.ldap.connect.pool", rb.getString("env.pool"));
		ldapenv.put(Context.PROVIDER_URL, rb.getString("env.providerurl"));
		ldapenv.put(Context.SECURITY_AUTHENTICATION, rb.getString("env.securityauth"));
		ldapenv.put(Context.SECURITY_PRINCIPAL, rb.getString("env.securityprincipal"));
		ldapenv.put(Context.SECURITY_CREDENTIALS, rb.getString("env.securitycredentials"));
		ldapenv.put(Context.INITIAL_CONTEXT_FACTORY, rb.getString("env.initialcontextfactory"));	
		
		logger.info("ldap connection url: " + rb.getString("env.providerurl"));
		
		logger.info("com.sun.jndi.ldap.connect.pool.authentication: " +
	            System.getProperty("com.sun.jndi.ldap.connect.pool.authentication"));
		logger.info("com.sun.jndi.ldap.connect.pool.protocol: " +
				System.getProperty("com.sun.jndi.ldap.connect.pool.protocol"));
		logger.info("com.sun.jndi.ldap.connect.pool.debug: " +
				System.getProperty("com.sun.jndi.ldap.connect.pool.debug"));
		logger.info("com.sun.jndi.ldap.connect.pool.initsize: " +
				System.getProperty("com.sun.jndi.ldap.connect.pool.initsize"));
		logger.info("com.sun.jndi.ldap.connect.pool.maxsize: " + 
				System.getProperty("com.sun.jndi.ldap.connect.pool.maxsize"));
		logger.info("com.sun.jndi.ldap.connect.pool.prefsize: " +
				System.getProperty("com.sun.jndi.ldap.connect.pool.prefsize"));
		logger.info("com.sun.jndi.ldap.connect.pool.timeout: " +
				System.getProperty("com.sun.jndi.ldap.connect.pool.timeout"));		
		
	}

    public static LDAPUtil getInstance(String env) {
        
        if (ldaputil == null) {
            
            synchronized (LDAPUtil.class) {
                
                if (ldaputil == null) {
                    ldaputil = new LDAPUtil(env);
                }
                
            }
            
        }
        
        return ldaputil;
    }
    
    
    public static DirContext getContext() {
    	
    	DirContext ctx = null;
        
        	
		try {
			
			
			ctx = new InitialDirContext(ldapenv);
			
		} catch (NamingException e) {
			logger.error("Did NOT get a LDAP Context", e); 
		}
			

  
        return ctx;
    }
    
    
    
    public static void releaseContext(DirContext ctx, NamingEnumeration<SearchResult> answer) {
        
    	if (answer != null) {
            try {
				answer.close();
			} catch (NamingException e) {
				logger.error("LDAP answer not closed", e);  
			}
            answer = null;
        }
        
        if (ctx != null) {
            try {
				ctx.close();
			} catch (NamingException e) {
				logger.error("LDAP Context not closed", e);  
			}
            ctx = null;
        }
        
    } 

    private static String getAttributeValue(Attributes attributes, String attributeName) {
    	
    	String returnVal = "";
    	
    	Attribute attr = attributes.get(attributeName);
    	
    	if (attr != null) {
    		try {
				returnVal = (String)attr.get();
			} catch (NamingException e) {
				logger.warn("ERROR: Unable to retrieve LDAP attribute entry for: " + attributeName, e); 			
				returnVal = "";
			}
    	}   	
    	
    	return returnVal;
    	
    }
    
    
    public static boolean login(String userId, String password) {
    	
    	boolean validLogin = false;
    	
		ResourceBundle rb = ResourceBundle.getBundle("com.cisco.bccsp.ldap.ldap_dev");
		 	
		Hashtable<String,String> loginenv = new Hashtable<String,String>();
		
		String securityPrinciple = MessageFormat.format(rb.getString("env.loginsecurityprincipal"), userId);
		
		logger.info("trying to log in: " + userId);
		
		loginenv.put(Context.PROVIDER_URL, rb.getString("env.providerurl"));
		loginenv.put(Context.SECURITY_AUTHENTICATION, rb.getString("env.securityauth"));
		loginenv.put(Context.SECURITY_PRINCIPAL, securityPrinciple);
		loginenv.put(Context.SECURITY_CREDENTIALS, password);
		loginenv.put(Context.INITIAL_CONTEXT_FACTORY, rb.getString("env.initialcontextfactory")); 
	
    	DirContext ctx = null;
		try {
			ctx = new InitialDirContext(loginenv);
			validLogin = true;
			ctx.close();
		} catch (NamingException e) {
			logger.warn("Did not get a LDAP Context for login, invalid login"); 
		}   
		
    	return validLogin;
    	
    }   
    
    
    public static User getLDAPPerson(String userId) {
        
        User user = null;
        
        DirContext ctx = getContext();
        NamingEnumeration<SearchResult> answer = null;
        
        if (ctx != null) {
        
   	try {

   	// Specify the ids of the attributes to return
   	String[] attrIDs = { "displayName", "givenName", "sn", "description",
   	"telephoneNumber", "l", "department", "title" };

   	SearchControls ctls = new SearchControls();
  // 	ctls.setReturningAttributes(attrIDs);

   	// Specify the search scope
   	ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

   	// Specify the search filter to match
   	String filter = "(&(cn=" + userId
   	+ ")(objectclass=user)(objectCategory=person))";

   	// Specify the base dn
   	String baseDN = "DC=cisco, DC=com";

   	// Search the subtree for objects by using the filter
   	answer = ctx.search(baseDN, filter, ctls);

   	// Get the objects from the search result
   	if (answer.hasMore()) {

   	SearchResult sr = (SearchResult) answer.next();
   	Attributes attrs = sr.getAttributes();

   	user = new User();


   	user.setDescription(getAttributeValue(attrs, "description"));
   	user.setUserId(getAttributeValue(attrs, "cn"));
   	user.setLocation(getAttributeValue(attrs, "l"));

   	user.setTitle(getAttributeValue(attrs, "title"));

   	} 


   	} catch (NamingException e) {
   	logger.warn("Unable to retrieve LDAP entry for: " + userId, e); 
   	} finally {
   	releaseContext(ctx, answer);
   	}


        }
        
        return user;
        
       }
    
    public static String verifyLDAPPerson(String userId) {
        
User user = null;
String value="false";
        
        DirContext ctx = getContext();
        NamingEnumeration<SearchResult> answer = null;
        
        if (ctx != null) {
        
   	try {

   	// Specify the ids of the attributes to return
   	String[] attrIDs = { "displayName", "givenName", "sn", "description",
   	"telephoneNumber", "l", "department", "title" };

   	SearchControls ctls = new SearchControls();
  // 	ctls.setReturningAttributes(attrIDs);

   	// Specify the search scope
   	ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

   	// Specify the search filter to match
   	String filter = "(&(cn=" + userId
   	+ ")(objectclass=user)(objectCategory=person))";

   	// Specify the base dn
   	String baseDN = "DC=cisco, DC=com";

   	// Search the subtree for objects by using the filter
   	answer = ctx.search(baseDN, filter, ctls);

   	// Get the objects from the search result
   	if (answer.hasMore()) {

   	SearchResult sr = (SearchResult) answer.next();
   	Attributes attrs = sr.getAttributes();

   	user = new User();


   	user.setDescription(getAttributeValue(attrs, "description"));
   	user.setUserId(getAttributeValue(attrs, "cn"));
   	user.setLocation(getAttributeValue(attrs, "l"));

   	user.setTitle(getAttributeValue(attrs, "title"));
   	value="true";
   	} 


   	} catch (NamingException e) {
   	logger.warn("Unable to retrieve LDAP entry for: " + userId, e); 
   	} finally {
   	releaseContext(ctx, answer);
   	}


        }
        
        return value;
        
       }
    
    
    
    public static List<groupMemberLDAP> getGroupDetails(groupMemberLDAP grpper) {
        String memberOf =grpper.getMemberOf();
        String ldapgroup=grpper.getUserId();
        groupMemberLDAP user = null;
        List<groupMemberLDAP> user_list=new ArrayList<groupMemberLDAP>();
        DirContext ctx = getContext();
        NamingEnumeration<SearchResult> answer = null;
        
        if (ctx != null) {
        
   	try {

   		
   	 	
   	 	  

   	// Specify the ids of the attributes to return
   	String[] attrIDs = { "displayName", "givenName","name", "sn", "description",
   	"telephoneNumber", "l", "department", "title" };

   	SearchControls ctls = new SearchControls();
   	//ctls.setReturningAttributes(attrIDs);
   	String filter="";
   
   	if(ldapgroup.equalsIgnoreCase("activedirectory"))
   	{
   		filter = "memberOf=CN="+memberOf+",OU=Standard,OU=Cisco Groups,DC=dev,DC=cisco,DC=com";
   			   
   		
   		
   	}
   	else
   	{
   		
   		filter = "(|(memberOf=CN="+memberOf+",OU=Mailer,OU=Cisco Groups,DC=dev,DC=cisco,DC=com)(memberOf=CN="+memberOf+",OU=Organizational,OU=Cisco Groups,DC=dev,DC=cisco,DC=com)(memberOf=CN="+memberOf+",OU=Grouper,OU=Cisco Groups,DC=dev,DC=cisco,DC=com))";   	
   	
   	}
	//System.out.println(filter);
   	// Specify the search scope
	
   	ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
   	
   	// Specify the search filter to match
   
   	// Specify the base dn
   	String baseDN = "dc=cisco,dc=com";

   	// Search the subtree for objects by using the filter
   	
   	answer = ctx.search(baseDN, filter, ctls);
  
   	// Get the objects from the search result
   	while (answer.hasMore()) {
   	user = new groupMemberLDAP();
   	SearchResult sr = (SearchResult) answer.next();
   	Attributes attrs = sr.getAttributes();
   	
   	Attribute attr=attrs.get("displayName");
   	String temp=(String)attr.get();
   	user.setUserName(temp.substring(0, temp.indexOf('(')).trim());
   	attr=attrs.get("name");
   	temp=(String)attr.get();
	user.setUserID(temp.trim());
	user.setConflict(checkUserPermissionConflict.verify(temp.trim(),grpper));
	attr=attrs.get("title");
   	temp=(String)attr.get();
	user.setTitle(temp.trim());
   	user_list.add(user);


   	
   	} 


   	} catch (NamingException e) {
   	logger.warn("Unable to retrieve LDAP entry for: " , e); 
   	} finally {
   	releaseContext(ctx, answer);
   	}


        }
        
        return user_list;
        
       }
    
}