package org.bimserver.cobie.utils;
/******************************************************************************
 * Copyright (C) 2011  ERDC
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *****************************************************************************/
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.xmlbeans.SchemaParticle;
import org.bimserver.cobie.cobielite.COBIEType;
import org.bimserver.cobie.cobielite.CategoryPickLists;
import org.bimserver.cobie.cobielite.ComponentType;
import org.bimserver.cobie.cobielite.DocumentType;
import org.bimserver.cobie.cobielite.SpaceType;
import org.bimserver.cobie.cobielite.TypeType;
import org.bimserver.cobie.utils.COBieUtility.CobieSheetName;
import org.bimserver.models.ifc2x3tc1.IfcProduct;

public class COBieQuery
{
	public static ArrayList<DocumentType> documentsForCOBieRow
	(COBIEType.Documents documents,COBieUtility.CobieSheetName sheet, String rowKey)
	{
		ArrayList<DocumentType> matchingDocuments =
				new ArrayList<DocumentType>();
		String sheetName = sheet.toString();
		for(DocumentType document : documents.getDocumentArray())
		{
			if (document.getSheetName().equalsIgnoreCase(sheetName) &&
					document.getRowName().equalsIgnoreCase(rowKey))
				matchingDocuments.add(document);
		}
		return matchingDocuments;
	}
	
	public static boolean isComponentInCOBie(IfcProduct product, COBIEType cobie)
	{
		boolean isInCOBie = false;
		String componentName = product.getName();
		String guid="";
		try
		{
			guid = product.getGlobalId().getWrappedValue();
		}
		catch(Exception e)
		{
			
		}
		for(ComponentType component : cobie.getComponents().getComponentArray())
		{
			if (component.getName().equals(componentName) && component.getExtIdentifier().equals(guid))
				isInCOBie = true;
		}
		return isInCOBie;
	}
	
	public static boolean isNameInComponent(String name, COBIEType cobie)
	{
		boolean isInComponent = false;
		try
		{
			COBIEType.Components components = cobie.getComponents();
			for(ComponentType component : components.getComponentArray())
				if (component.getName().equals(name))
					isInComponent=true;
		}
		catch(Exception e)
		{
			
		}
		return isInComponent;
	}
	
	public static boolean isNameInType(String name, COBIEType cobie)
	{
		boolean isInType = false;
		try
		{
			COBIEType.Types types = cobie.getTypes();
			for(TypeType type: types.getTypeArray())
				if (type.getName().equals(name))
					isInType=true;
		}
		catch(Exception e)
		{
			
		}
		return isInType;
	}
	
	public static String spaceNameFromGuid(String guid, COBIEType.Spaces spaces)
	{
		String name = "";
		for(SpaceType space : spaces.getSpaceArray())
		{
			if (space.getExtIdentifier().equals(guid) && name.length()==0)
				name = space.getName();
		}
		return COBieUtility.getCOBieString(name);
	}
	
	public static String pickListCategoryColumnNameFromSheetName(COBieUtility.CobieSheetName sheetName) throws Exception
	{
		try
		{
			String pickListColName = "";
			SchemaParticle particle = CategoryPickLists.type.getContentModel();
			for(SchemaParticle childParticle: particle.getParticleChildren())
			{
				String childName = childParticle.getName().getLocalPart();
				if (childName.equalsIgnoreCase(sheetName.name()) && childParticle.isFixed())
				{
					pickListColName = childParticle.getDefaultText();
				}
			}
			if (pickListColName.length()==0)
				throw new Exception("No matching sheet name in CategoryPickLists");
			return pickListColName;
		}
		catch(Exception e)
		{
			throw e;
		}
		
	}
	
	public static HashMap<String,String> getSheetNamesPickListCategory()
	{
		HashMap<String,String> sheetNames = new HashMap<String,String>();
		SchemaParticle particle = CategoryPickLists.type.getContentModel();
		for(CobieSheetName sheetName: COBieUtility.CobieSheetName.values())
		{
			try
			{
				String pickListCol = COBieQuery.pickListCategoryColumnNameFromSheetName(sheetName);
				sheetNames.put(sheetName.name(),pickListCol);
				
			}
			catch(Exception e)
			{
				
			}
		}
		return sheetNames;
	}
	public static HashMap<CobieSheetName,String> getCobieSheetNamesPickListCategory()
	{
		HashMap<CobieSheetName,String> sheetNames = new HashMap<CobieSheetName,String>();
		SchemaParticle particle = CategoryPickLists.type.getContentModel();
		for(CobieSheetName sheetName: COBieUtility.CobieSheetName.values())
		{
			try
			{
				String pickListCol = COBieQuery.pickListCategoryColumnNameFromSheetName(sheetName);
				sheetNames.put(sheetName,pickListCol);
				
			}
			catch(Exception e)
			{
				
			}
		}
		return sheetNames;
	}
}
