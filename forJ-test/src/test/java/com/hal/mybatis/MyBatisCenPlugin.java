package com.hal.mybatis;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;

public class MyBatisCenPlugin extends PluginAdapter {
    private ShellCallback shellCallback = null;
//    private FullyQualifiedJavaType serializable;
    private String searchString;
    private String replaceString;
    private Pattern pattern;

    private String voTargetPackage;
    private String voTargetProject;
    
    private String extMapperTargetPackage;
    private String extMapperTargetProject;
    private String extSqlMapTargetProject;
    private String extMapperSuffix;
    
    public MyBatisCenPlugin() {
    	super();
//    	serializable = new FullyQualifiedJavaType("java.io.Serializable"); //$NON-NLS-1$
        shellCallback = new DefaultShellCallback(false);
	}
    
    public boolean validate(List<String> warnings) {

        searchString = properties.getProperty("searchString"); //$NON-NLS-1$
        replaceString = properties.getProperty("replaceString"); //$NON-NLS-1$
        voTargetPackage = properties.getProperty("voTargetPackage");
        voTargetProject = properties.getProperty("voTargetProject"); 
        extMapperTargetPackage = properties.getProperty("extMapperTargetPackage");
        extMapperTargetProject = properties.getProperty("extMapperTargetProject"); 
        extSqlMapTargetProject = properties.getProperty("extSqlMapTargetProject"); 
        extMapperSuffix = properties.getProperty("extMapperSuffix", "");

        boolean valid = stringHasValue(searchString)
                && stringHasValue(replaceString);

        if (valid) {
            pattern = Pattern.compile(searchString);
        } else {
            if (!stringHasValue(searchString)) {
                warnings.add(getString("ValidationError.18", //$NON-NLS-1$
                        "RenameExampleClassPlugin", //$NON-NLS-1$
                        "searchString")); //$NON-NLS-1$
            }
            if (!stringHasValue(replaceString)) {
                warnings.add(getString("ValidationError.18", //$NON-NLS-1$
                        "RenameExampleClassPlugin", //$NON-NLS-1$
                        "replaceString")); //$NON-NLS-1$
            }
        }

        return valid;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
    	// 改java mapper名
        String oldType = introspectedTable.getMyBatis3JavaMapperType();
        Matcher matcher = pattern.matcher(oldType);
        oldType = matcher.replaceAll(replaceString);
        introspectedTable.setMyBatis3JavaMapperType(oldType);
        // 改 xml 名
        String xmlName = introspectedTable.getMyBatis3XmlMapperFileName();
        xmlName = pattern.matcher(xmlName).replaceAll(replaceString);
        introspectedTable.setMyBatis3XmlMapperFileName(xmlName);
        
        try {
            File mapperDir = shellCallback.getDirectory(context.getSqlMapGeneratorConfiguration().getTargetProject(),
            		context.getSqlMapGeneratorConfiguration().getTargetPackage());
            File mapperFile = new File(mapperDir, xmlName);
            // xml文件存在,删除
            if (mapperFile.exists()) {
            	mapperFile.delete();
            }
        } catch (ShellException e) {
            e.printStackTrace();
        }
//        
    }
    

    public boolean modelFieldGenerated(Field field,
            TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable,
            Plugin.ModelClassType modelClassType) {
    	// 加注释
    	String doc = introspectedColumn.getRemarks();
    	doc = doc == null ? "" : doc;
    	field.addJavaDocLine("/** "+doc+" */");
        return true;
    }

    public boolean modelGetterMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable,
            Plugin.ModelClassType modelClassType) {
    	// 加注释
    	String doc = introspectedColumn.getRemarks();
    	doc = doc == null ? "" : doc;
    	method.addJavaDocLine("/** "+doc+" */");
        return true;
    }
    
    public boolean modelSetterMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable,
            Plugin.ModelClassType modelClassType) {
    	// 加注释
    	String doc = introspectedColumn.getRemarks();
    	doc = doc == null ? "" : doc;
    	method.addJavaDocLine("/** "+doc+" */");
        return true;
    }
    

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> javaFiles = new ArrayList<GeneratedJavaFile>();
        System.out.println("-----------");
        System.out.println(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        System.out.println(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        System.out.println(introspectedTable.getPrimaryKeyType());
        System.out.println(introspectedTable.getMyBatis3FallbackSqlMapNamespace());
        System.out.println(introspectedTable.getExampleType());
        System.out.println(introspectedTable.getRecordWithBLOBsType());
        System.out.println(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        System.out.println(introspectedTable.getBaseRecordType());
        System.out.println(introspectedTable.getFullyQualifiedTable().getIbatis2SqlMapNamespace());
        
        if(stringHasValue(voTargetPackage) && stringHasValue(voTargetProject)){
        	GeneratedJavaFile voJavafile = makeVo(introspectedTable);
        	if (voJavafile != null) {
                javaFiles.add(voJavafile);
			}
        }
        
        if(stringHasValue(extMapperTargetPackage) && stringHasValue(extMapperTargetProject)){
        	GeneratedJavaFile extMapperJavafile = makeExtMapper(introspectedTable);
        	if (extMapperJavafile != null) {
        		javaFiles.add(extMapperJavafile);
        	}
        }
        
        return javaFiles;
    }
    
    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
    	List<GeneratedXmlFile> xmlFiles = new ArrayList<GeneratedXmlFile>();

        if(stringHasValue(extMapperTargetPackage) 
        		&& stringHasValue(extMapperTargetProject)
        		&& stringHasValue(extSqlMapTargetProject)){
        	GeneratedXmlFile extSqlfile = makeExtSqlXml(introspectedTable);
        	if (extSqlfile != null) {
        		xmlFiles.add(extSqlfile);
        	}
        }
    	return xmlFiles;
    };
    
    GeneratedJavaFile makeVo(IntrospectedTable introspectedTable){
    	String voName = introspectedTable.getFullyQualifiedTable().getDomainObjectName() + "Vo";
    	TopLevelClass voType = new TopLevelClass(voTargetPackage + "." + voName);
    	voType.setVisibility(JavaVisibility.PUBLIC);
        voType.addImportedType(introspectedTable.getBaseRecordType());
        voType.setSuperClass(introspectedTable.getBaseRecordType());
//        voType.addImportedType(serializable);
//        voType.addSuperInterface(serializable);

        Field field = new Field();
        field.setFinal(true);
        field.setInitializationString("1L"); //$NON-NLS-1$
        field.setName("serialVersionUID"); //$NON-NLS-1$
        field.setStatic(true);
        field.setType(new FullyQualifiedJavaType("long")); //$NON-NLS-1$
        field.setVisibility(JavaVisibility.PRIVATE);
        context.getCommentGenerator().addFieldComment(field, introspectedTable);
        voType.addField(field);
        try {
            GeneratedJavaFile voJavafile = new GeneratedJavaFile(voType, voTargetProject, context.getJavaFormatter());
            File mapperDir = shellCallback.getDirectory(voTargetProject, voTargetPackage);
            File mapperFile = new File(mapperDir, voJavafile.getFileName());
            // 文件不存在
            if (!mapperFile.exists()) {
            	return voJavafile;
            }
        } catch (ShellException e) {
            e.printStackTrace();
        }
        return null;
    }
    

    GeneratedJavaFile makeExtMapper(IntrospectedTable introspectedTable){
    	String extMapperName = introspectedTable.getFullyQualifiedTable().getDomainObjectName() + extMapperSuffix;
    	Interface interfaze = new Interface(extMapperTargetPackage + "." + extMapperName);
    	interfaze.setVisibility(JavaVisibility.PUBLIC);
        try {
            GeneratedJavaFile voJavafile = new GeneratedJavaFile(interfaze, extMapperTargetProject, context.getJavaFormatter());
            File mapperDir = shellCallback.getDirectory(extMapperTargetProject, extMapperTargetPackage);
            File mapperFile = new File(mapperDir, voJavafile.getFileName());
            System.out.println(mapperFile + " -- " + mapperFile.exists());
            if (!mapperFile.exists()) {
            	return voJavafile;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    GeneratedXmlFile makeExtSqlXml(IntrospectedTable introspectedTable){
    	String extMapperName = introspectedTable.getFullyQualifiedTable().getDomainObjectName() + extMapperSuffix;
    	
    	
    	Document document = new Document("-//mybatis.org//DTD Mapper 3.0//EN", "http://mybatis.org/dtd/mybatis-3-mapper.dtd");
    	XmlElement rootElement = new XmlElement("mapper");
    	rootElement.addAttribute(new Attribute("namespace", extMapperTargetPackage + "." +extMapperName));
    	document.setRootElement(rootElement);

    	XmlElement resultMap = new XmlElement("resultMap");
    	resultMap.addAttribute(new Attribute("id", "BaseResultMap"));
    	resultMap.addAttribute(new Attribute("type", introspectedTable.getBaseRecordType()));
    	resultMap.addAttribute(new Attribute("extends", introspectedTable.getMyBatis3JavaMapperType() + ".BaseResultMap"));
    	rootElement.addElement(resultMap);

    	XmlElement sql = new XmlElement("sql");
    	sql.addAttribute(new Attribute("id", "Base_Column_List"));
    	XmlElement include = new XmlElement("include");
    	include.addAttribute(new Attribute("refid", introspectedTable.getMyBatis3JavaMapperType() + ".Base_Column_List"));
    	sql.addElement(include);
    	rootElement.addElement(sql);
    	
        try {
        	GeneratedXmlFile xmlfile = new GeneratedXmlFile(document, extMapperName+".xml", extMapperTargetPackage, extSqlMapTargetProject, false, context.getXmlFormatter());

            File dir = shellCallback.getDirectory(extSqlMapTargetProject, extMapperTargetPackage);
            File file = new File(dir, xmlfile.getFileName());
            if (!file.exists()) {
            	return xmlfile;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
