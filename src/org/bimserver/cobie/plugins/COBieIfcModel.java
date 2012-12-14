package org.bimserver.cobie.plugins;
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
import java.util.Map;

import org.bimserver.cobie.cobielite.ComponentType;
import org.bimserver.cobie.utils.COBieUtility;
import org.bimserver.cobie.utils.cobiewriters.IfcToFacility;
import org.bimserver.cobie.utils.cobiewriters.IfcToFloor;
import org.bimserver.cobie.utils.cobiewriters.IfcToSpace;
import org.bimserver.cobie.utils.deserializer.IfcCommonHandler;
import org.bimserver.cobie.utils.deserializer.SystemDeserializer;
import org.bimserver.cobie.utils.deserializer.ZoneDeserializer;
import org.bimserver.cobie.utils.stringwriters.DeserializerStaticStrings;
import org.bimserver.emf.IdEObject;
import org.bimserver.emf.IfcModelInterfaceException;
import org.bimserver.emf.OidProvider;
import org.bimserver.ifc.IfcModel;
import org.bimserver.models.ifc2x3tc1.Ifc2x3tc1Factory;
import org.bimserver.models.ifc2x3tc1.IfcBuilding;
import org.bimserver.models.ifc2x3tc1.IfcBuildingStorey;
import org.bimserver.models.ifc2x3tc1.IfcObjectDefinition;
import org.bimserver.models.ifc2x3tc1.IfcOwnerHistory;
import org.bimserver.models.ifc2x3tc1.IfcPersonAndOrganization;
import org.bimserver.models.ifc2x3tc1.IfcProduct;
import org.bimserver.models.ifc2x3tc1.IfcPropertyEnumeration;
import org.bimserver.models.ifc2x3tc1.IfcRelAggregates;
import org.bimserver.models.ifc2x3tc1.IfcRelContainedInSpatialStructure;
import org.bimserver.models.ifc2x3tc1.IfcRelDefinesByType;
import org.bimserver.models.ifc2x3tc1.IfcSpace;
import org.bimserver.models.ifc2x3tc1.IfcSystem;
import org.bimserver.models.ifc2x3tc1.IfcTypeObject;
import org.bimserver.models.ifc2x3tc1.IfcZone;

import com.google.common.collect.BiMap;

public class COBieIfcModel extends IfcModel {

	private static final String IFC_DOOR_ENTITY_NAME = "IfcDoor";

	private static final String IFC_WINDOW_ENTITY_NAME = "IfcWindow";

	private Map<String, Long> componentNameToOid;

	private Map<String, Long> EmailToIfcOwnerHistoryOid;

	private Map<String, Long> EmailToPersonAndOrganizationOid;
		
	private Map<String, Long> enumerationNameToOid;
	
	private Map<String, Long> FacilityNameToOid;
	
	private Map<String, Long> zoneNameCategoryToOid;
	
	private Map<String, Long> FloorNameToOid;
	
	private Map<String, ArrayList<String>> FloorNameToSpaceNames;
	
	private Map<String, ArrayList<String>> spaceNameToComponentNames;
	
	private ArrayList<String> componentNamesNotInSpaces;
	private ArrayList<String> componentNamesAssignedToASpace;
	private Map<String, Long> SpaceNameToOid;

	private Map<String,Long> systemNameCategoryToOid;
	private Map<String, ArrayList<String>> typeNameToComponentNames;

	private Map<String, Long> TypeNameToOid;

	private Map<String, Long> unitNameToOid;

	private Long firstFacilityOid;

	private static ArrayList<String> getAllowMultipeSpaceComponentTypes()
	{
		ArrayList<String> allowList = new ArrayList<String>();
		allowList.add(IFC_WINDOW_ENTITY_NAME);
		allowList.add(IFC_DOOR_ENTITY_NAME);
		return allowList;
	}
	public COBieIfcModel() {
		super();
		initializeHashMaps();
		// TODO Auto-generated constructor stub
	}

	public COBieIfcModel(BiMap<Long, IdEObject> objects) {
		super(objects);
		// TODO Auto-generated constructor stub
	}

	public COBieIfcModel(int size) {
		super(size);
		initializeHashMaps();
		// TODO Auto-generated constructor stub
	}
	
	public long add(IdEObject eObject, OidProvider oidProvider) 
	{
		long oid = oidProvider.newOid(eObject.eClass());
		try {
			this.add(oid, eObject);
		} catch (IfcModelInterfaceException ex) {
			// TODO Cobie change
			ex.printStackTrace();
		}
		objectAdded(eObject,oid);
		return oid;
	}
	
	public long addComponent(IfcProduct product, ComponentType component, IfcCommonHandler ifcCommonHandler)
	{
		long oid = ifcCommonHandler.getOidProvider().newOid(product.eClass());
		try {
			this.add(oid, product);
		} catch (IfcModelInterfaceException ex) {
			// TODO Cobie change
			ex.printStackTrace();
		}
		componentAdded(product,oid,component,ifcCommonHandler);
		return oid;
	}

	
	private boolean multipleSpacesAllowed(ComponentType component)
	{
		boolean multipleSpacesAllowed = false;
		if(COBieIfcModel.getAllowMultipeSpaceComponentTypes().contains(component.getExtObject()))
			multipleSpacesAllowed = true;
		return multipleSpacesAllowed;
	}
	
	private void assignComponentToSpace(ComponentType component)
	{
		
		String componentName = component.getName();
		String spaceName = component.getSpace();
		boolean compAllowsMultipleSpaces = multipleSpacesAllowed(component);
		handleAssignComponentToSpace(componentName, spaceName,
				compAllowsMultipleSpaces);
		
	}
	private void handleAssignComponentToSpace(String componentName,
			String spaceName, boolean compAllowsMultipleSpaces)
	{
		if (!COBieUtility.isNA(spaceName))
		{
			ArrayList<String> spaceComponents = new ArrayList<String>();
			if (SpaceNameToOid.containsKey(spaceName))
			{
				if (spaceNameToComponentNames.containsKey(spaceName))
					spaceComponents = spaceNameToComponentNames.get(spaceName);
				else
					spaceComponents = new ArrayList<String>();
				if (!spaceComponents.contains(componentName))
				{
					spaceComponents.add(componentName);
					spaceNameToComponentNames.put(spaceName, spaceComponents);
					if(!componentNamesAssignedToASpace.contains(componentName))
						this.componentNamesAssignedToASpace.add(componentName);
					if(componentNamesNotInSpaces.contains(componentName))
						componentNamesNotInSpaces.remove(componentName);
				}
			}
			else if (compAllowsMultipleSpaces && spaceName.contains(COBieUtility.getCOBieDelim()))
			{
				ArrayList<String> spaceNames = COBieUtility.arrayListFromDelimString(spaceName);
				for(String tmpSpaceName:spaceNames)
				{
					handleAssignComponentToSpace(componentName,tmpSpaceName,false);
				}
			}
			
			else
			{
				if(!componentNamesAssignedToASpace.contains(componentName))
					this.componentNamesNotInSpaces.add(componentName);
			}
		
		}
		else
		{
			if(!componentNamesAssignedToASpace.contains(componentName))
				this.componentNamesNotInSpaces.add(componentName);
		}
	}

	public void setSpaceAggregates(IfcCommonHandler ifcCommonHandler)
	{
		ArrayList<String> ComponentNames;
		long SpaceOid, ComponentOid;
		for (String SpaceName : spaceNameToComponentNames.keySet())
		{
			if (SpaceNameToOid.containsKey(SpaceName))
			{
				SpaceOid = SpaceNameToOid.get(SpaceName);
				IfcSpace Space = (IfcSpace) this.get(SpaceOid);
				ComponentNames = spaceNameToComponentNames.get(SpaceName);
				IfcRelContainedInSpatialStructure aggregatesSpace = Ifc2x3tc1Factory.eINSTANCE
						.createIfcRelContainedInSpatialStructure();
				// IfcRelAggregates aggregatesSpace =
				// Ifc2x3tc1Factory.eINSTANCE.createIfcRelAggregates();
				aggregatesSpace.setName(DeserializerStaticStrings
						.getStoreyRelAggregatesName());
				aggregatesSpace.setDescription(DeserializerStaticStrings
						.getStoreyRelAggregatesDescription());
				aggregatesSpace.setOwnerHistory(ifcCommonHandler.getOwnerHistoryHandler().DefaultOwnerHistory());
				aggregatesSpace.setGlobalId(ifcCommonHandler.getGuidHandler().newGuid());
				aggregatesSpace.setRelatingStructure(Space);
				// aggregatesSpace.setRelatingObject(Space);
				if (ComponentNames != null)
				{
					for (String ComponentName : ComponentNames)
					{
						ComponentOid = componentNameToOid.get(ComponentName);
						IfcProduct component = (IfcProduct) this.get(ComponentOid);
						// aggregatesSpace.getRelatedObjects().add(component);
						aggregatesSpace.getRelatedElements().add(component);
					}
				}
				this.add(aggregatesSpace, ifcCommonHandler.getOidProvider());
			}
		}
	}
	
	public void setFacilityComponentAggregates(IfcCommonHandler ifcCommonHandler) {
		long ComponentOid;
		long FacilityOid = this.firstFacilityOid;

		IfcBuilding facility = (IfcBuilding) this.get(FacilityOid);
		IfcRelContainedInSpatialStructure aggregatesFacility = Ifc2x3tc1Factory.eINSTANCE
				.createIfcRelContainedInSpatialStructure();
		// IfcRelAggregates aggregatesSpace =
		// Ifc2x3tc1Factory.eINSTANCE.createIfcRelAggregates();
		aggregatesFacility.setName(DeserializerStaticStrings
				.getStoreyRelAggregatesName());
		aggregatesFacility.setDescription(DeserializerStaticStrings
				.getStoreyRelAggregatesDescription());
		aggregatesFacility.setOwnerHistory(ifcCommonHandler
				.getOwnerHistoryHandler().DefaultOwnerHistory());
		aggregatesFacility.setGlobalId(ifcCommonHandler.getGuidHandler()
				.newGuid());
		aggregatesFacility.setRelatingStructure(facility);
		// aggregatesSpace.setRelatingObject(Space);
		if (this.componentNamesNotInSpaces != null) {
			for (String ComponentName : componentNamesNotInSpaces) {
				ComponentOid = componentNameToOid.get(ComponentName);
				IfcProduct component = (IfcProduct) this.get(ComponentOid);
				// aggregatesSpace.getRelatedObjects().add(component);
				aggregatesFacility.getRelatedElements().add(component);
			}
		}
		this.add(aggregatesFacility, ifcCommonHandler.getOidProvider());
	}
	
	public void setComponentTypeRelations(IfcCommonHandler ifcCommonHandler)
	{
		ArrayList<String> ComponentNames;
		long TypeOid, ComponentOid, RelOid;
		for (String TypeName : typeNameToComponentNames.keySet())
		{
			TypeOid = TypeNameToOid.get(TypeName);
			IfcTypeObject typeObject = (IfcTypeObject) this.get(TypeOid);
			ComponentNames = typeNameToComponentNames.get(TypeName);
			IfcRelDefinesByType defByType = Ifc2x3tc1Factory.eINSTANCE
					.createIfcRelDefinesByType();
			defByType.setName(DeserializerStaticStrings
					.getComponentRelDefinesByTypeName());
			defByType.setDescription(DeserializerStaticStrings
					.getComponentRelDefinesByTypeDescription());
			defByType.setOwnerHistory(ifcCommonHandler.getOwnerHistoryHandler().DefaultOwnerHistory());
			defByType.setGlobalId(ifcCommonHandler.getGuidHandler().newGuid());
			defByType.setRelatingType(typeObject);
			if (ComponentNames != null)
			{
				for (String ComponentName : ComponentNames)
				{
					ComponentOid = componentNameToOid.get(ComponentName);
					IfcProduct component = (IfcProduct) this.get(ComponentOid);
					defByType.getRelatedObjects().add(component);

				}
				RelOid = this.add(defByType, ifcCommonHandler.getOidProvider());
				for (String ComponentName : ComponentNames)
				{
					ComponentOid = componentNameToOid.get(ComponentName);
					IfcProduct component = (IfcProduct) this.get(ComponentOid);
					component.getIsDefinedBy().add(
							(IfcRelDefinesByType) this.get(RelOid));
				}
			}

		}

	}
	
	private void assignComponentToType(ComponentType component)
	{
		ArrayList<String> typeComponents = new ArrayList<String>();
		String componentName = component.getName();
		String typeName = component.getTypeName();
		if (!COBieUtility.isNA(typeName) && TypeNameToOid.containsKey(typeName))
		{
			if (typeNameToComponentNames.containsKey(typeName))
				typeComponents = typeNameToComponentNames.get(typeName);
			if (!typeComponents.contains(componentName))
			{
				typeComponents.add(componentName);
				typeNameToComponentNames.put(typeName, typeComponents);
			}
		}
	}
	
	public boolean containsContact(String email)
	{
		return EmailToPersonAndOrganizationOid.containsKey(email);
	}

	public boolean containsCreatedBy(String email)
	{
		return EmailToIfcOwnerHistoryOid.containsKey(email);
	}
	
	public boolean containsFacility(String facilityName)
	{
		return FacilityNameToOid.containsKey(facilityName);
	}
	
	public boolean containsComponent(String componentName)
	{
		return this.componentNameToOid.containsKey(componentName);
	}

	public IfcPersonAndOrganization getContact(String email)
	{
		if (containsContact(email))
			return (IfcPersonAndOrganization) this.get(EmailToPersonAndOrganizationOid.get(email));
		else
			return null;
	}

	public Long getContactOid(String email)
	{
		if (EmailToPersonAndOrganizationOid.containsKey(email))
			return EmailToPersonAndOrganizationOid.get(email);
		else
			return null;
	}
	
	public Map<String,Long> getEmailToIfcOwnerHistoryOid()
	{
		return this.EmailToIfcOwnerHistoryOid;
	}

	public Map<String, Long> getEnumerationNameToOid()
	{
		return enumerationNameToOid;
	}

	public Map<String, Long> getFacilityNameToOid()
	{
		return FacilityNameToOid;
	}


	public Long getFacilityOid(String facilityName)
	{
		if (FacilityNameToOid.containsKey(facilityName))
			return FacilityNameToOid.get(facilityName);
		else 
			return null;
	}
	
	public Long getTypeOid(String typeName)
	{
		if (TypeNameToOid.containsKey(typeName))
			return TypeNameToOid.get(typeName);
		else
			return null;
	}
	
	public Long getComponentOid(String componentName)
	{
		if (this.componentNameToOid.containsKey(componentName))
			return componentNameToOid.get(componentName);
		else
			return null;
	}

	public Long getFloorOid(String floorName)
	{
		if (FloorNameToOid.containsKey(floorName))
			return FloorNameToOid.get(floorName);
		else
			return null;
	}


	public IfcOwnerHistory getOwnerHistory(String email)
	{
		if (containsCreatedBy(email))
			return (IfcOwnerHistory) this.get(EmailToIfcOwnerHistoryOid.get(email));
		else
			return null;
	}
	
	public IfcSpace getSpaceFromName(String spaceName)
	{
		return (IfcSpace) this.get(SpaceNameToOid.get(spaceName));
	}
	

	
	public ArrayList<String> getFloorNames()
	{
		ArrayList<String> floorNames = new ArrayList<String>();
		for(String key : this.FloorNameToOid.keySet())
			floorNames.add(key);
		return floorNames;
	}
	
	public ArrayList<String> getFloorSpaceNames(String floorName)
	{
		if (this.FloorNameToSpaceNames.containsKey(floorName))
			return this.FloorNameToSpaceNames.get(floorName);
		else
			return null;
	}
	

	public boolean containsSpace(String spaceName)
	{
		return SpaceNameToOid.containsKey(spaceName);
	}
	
	public Long getSpaceOid(String spaceName)
	{
		return SpaceNameToOid.get(spaceName);
	}
		
	public Map<String, Long> getUnitNameToOid()
	{
		return unitNameToOid;
	}

	public int getFacilityCount()
	{
		return FacilityNameToOid.size();
	}
	
	private void contactAdded(IfcPersonAndOrganization personOrg,Long oid)
	{
		String email = COBieUtility.getEmailFromPersonAndOrganization(personOrg);
		if (!containsContact(email))
			EmailToPersonAndOrganizationOid.put(email, oid);
	}
	
	private void createdByAdded(IfcOwnerHistory ownerHistory,Long oid)
	{
		String email = COBieUtility.getEmailFromOwnerHistory(ownerHistory);
		contactAdded(ownerHistory.getOwningUser(),oid);
		if (!containsCreatedBy(email))
			EmailToIfcOwnerHistoryOid.put(email, oid);
	}
	
	private void facilityAdded(IfcBuilding facility, Long oid)
	{
		String name = IfcToFacility.nameFromBuildign(facility);
		if (getFacilityCount()==0)
			this.firstFacilityOid = oid;
		if (!FacilityNameToOid.containsKey(name))
			FacilityNameToOid.put(name, oid);

	}
	
	private void componentAdded(IfcProduct product, Long oid,ComponentType component,IfcCommonHandler ifcCommonHandler)
	{
		if (!containsComponent(product.getName()))
		{
			this.componentNameToOid.put(product.getName(), oid);
			assignComponentToSpace(component);
			assignComponentToType(component);
		}
	}
	
	public Long getFirstFacilityOid()
	{
		return this.firstFacilityOid;
	}
	
	public IfcBuilding getFirstFacility()
	{
		return (IfcBuilding) this.get(getFirstFacilityOid());
	}
	
	private void initializeHashMaps()
	{
		setEmailToIfcOwnerHistoryOid(new HashMap<String,Long>());
		setEmailToPersonAndOrganizationOid(new HashMap<String,Long>());
		setEnumerationNameToOid(new HashMap<String,Long>());
		setUnitNameToOid(new HashMap<String,Long>());
		setFacilityNameToOid(new HashMap<String, Long>());
		setFloorNameToOid(new HashMap<String, Long>());
		setSpaceNameToOid(new HashMap<String, Long>());
		setTypeNameToOid(new HashMap<String, Long>());
		setFloorNameToSpaceNames(new HashMap<String, ArrayList<String>>());
		setComponentNameToOid(new HashMap<String, Long>());
		setSpaceNameToComponentNames(new HashMap<String, ArrayList<String>>());
		setTypeNameToComponentNames(new HashMap<String, ArrayList<String>>());
		setZoneNameToOid(new HashMap<String,Long>());
		setSystemNameCategoryToOid(new  HashMap<String,Long>());
		componentNamesNotInSpaces = new ArrayList<String>();
		componentNamesAssignedToASpace = new ArrayList<String>();
	}
	
	public void setZoneNameToOid(Map<String,Long> zoneNameMap)
	{
		this.zoneNameCategoryToOid = zoneNameMap;
	}
	private void objectAdded(IdEObject eObject,Long oid)
	{
		if(eObject instanceof IfcPersonAndOrganization)
			contactAdded((IfcPersonAndOrganization) eObject,oid);
		else if (eObject instanceof IfcOwnerHistory)
			createdByAdded((IfcOwnerHistory)eObject,oid);
		else if (eObject instanceof IfcBuilding)
			facilityAdded((IfcBuilding)eObject,oid);
		else if (eObject instanceof IfcBuildingStorey)
			floorAdded((IfcBuildingStorey)eObject,oid);
		else if (eObject instanceof IfcSpace)
			spaceAdded((IfcSpace)eObject,oid);
		else if (eObject instanceof IfcRelAggregates)
			relAggregatesAdded((IfcRelAggregates)eObject);
		else if (eObject instanceof IfcTypeObject)
			typeAdded((IfcTypeObject)eObject,oid);
		else if (eObject instanceof IfcZone)
			zoneAdded((IfcZone)eObject,oid);
		else if (eObject instanceof IfcSystem)
			systemAdded((IfcSystem)eObject,oid);
		else if (eObject instanceof IfcPropertyEnumeration)
			enumerationAdded((IfcPropertyEnumeration) eObject,oid);
	}
	
	private void enumerationAdded(IfcPropertyEnumeration enumeration, Long oid)
	{
		String enumName = enumeration.getName();
		if (enumName!=null)
		{
			if (!this.enumerationNameToOid.containsKey(enumName))
				enumerationNameToOid.put(enumName, oid);
		}
	}
	
	public boolean containsEnumeration(String enumName)
	{
		return enumerationNameToOid.containsKey(enumName);
	}
	
	public long getEnumerationOid(String enumName)
	{
		if (containsEnumeration(enumName))
			return enumerationNameToOid.get(enumName);
			else
				return -1;
	}
	
	private void systemAdded(IfcSystem eObject, Long oid) 
	{
		String systemKey = SystemDeserializer.systemKeyFromSystem(eObject);
		if (!this.systemNameCategoryToOid.containsKey(systemKey))
			this.systemNameCategoryToOid.put(systemKey, oid);
		
	}
	
	public boolean containsSystem(String systemKey)
	{
		return systemNameCategoryToOid.containsKey(systemKey);
	}

	private void zoneAdded(IfcZone zone, Long oid)
	{
		String zoneKey = ZoneDeserializer.getZoneKeyFromZone(zone);
		if (!this.zoneNameCategoryToOid.containsKey(zoneKey))
			this.zoneNameCategoryToOid.put(zoneKey, oid);
	}
	
	private void relAggregatesAdded(IfcRelAggregates relAggregates)
	{
		if(relAggregates.getRelatingObject() instanceof IfcBuildingStorey)
		{
			floorAggregateAdded(relAggregates);
		}
	}
	
	private void typeAdded(IfcTypeObject type,Long oid)
	{
		String typeName = type.getName();
		if (!this.TypeNameToOid.containsKey(typeName))
			TypeNameToOid.put(typeName, oid);
	}
	
	public boolean containsType(String typeName)
	{
		return this.TypeNameToOid.containsKey(typeName);
	}
	
	private void floorAggregateAdded(IfcRelAggregates relAggregates)
	{
		IfcBuildingStorey buildingStorey = (IfcBuildingStorey) relAggregates.getRelatingObject();
		for(IfcObjectDefinition obj : relAggregates.getRelatedObjects())
		{
			if (obj instanceof IfcSpace)
			{
				IfcSpace tmpSpace =(IfcSpace) obj;
				assignSpaceToFloor(tmpSpace, buildingStorey);
				
			}
		}
	}
	
	private void floorAdded(IfcBuildingStorey floor, Long oid)
	{
		String floorName = floor.getName();
		if (!FloorNameToOid.containsKey(floorName))
			FloorNameToOid.put(floorName, oid);
	}
	
	private void spaceAdded(IfcSpace space, Long oid)
	{
		String spaceName = space.getName();
		if(!SpaceNameToOid.containsKey(spaceName))
		{
			SpaceNameToOid.put(spaceName, oid);
		}
	}
	
	public boolean containsFloor(String floorName)
	{
		return this.FloorNameToOid.containsKey(floorName);
	}
	
	public boolean containsZone(String zoneName)
	{
		return (this.zoneNameCategoryToOid.containsKey(zoneName));
	}
	
	public long getZoneOid(String zoneName)
	{
		return (this.zoneNameCategoryToOid.get(zoneName));
	}
	
	public IfcBuildingStorey getFloorByName(String floorName)
	{
		if (containsFloor(floorName))
			return (IfcBuildingStorey) this.get(FloorNameToOid.get(floorName));
			else 
				return null;
	}
	
	
	private void assignSpaceToFloor(IfcSpace space,IfcBuildingStorey floor)
	{
		ArrayList<String> floorSpaces = new ArrayList<String>();
		String spaceName = IfcToSpace.nameFromSpace(space);
		String floorName = IfcToFloor.nameFromBuildingStorey(floor);
		if (floorName != null && floorName.length() > 0
				&& containsFloor(floorName))
		{
			if (FloorNameToSpaceNames.containsKey(floorName))
				floorSpaces = FloorNameToSpaceNames.get(floorName);
			floorSpaces.add(spaceName);
			FloorNameToSpaceNames.put(floorName, floorSpaces);
		}
	}
	
	public int getFloorCount()
	{
		return FloorNameToOid.size();
	}
	
	private void setComponentNameToOid(Map<String, Long> componentNameToOid)
	{
		this.componentNameToOid = componentNameToOid;
	}
	private void setEmailToIfcOwnerHistoryOid(Map<String,Long> emailToIfcOwnerHistoryOid)
	{
		this.EmailToIfcOwnerHistoryOid = emailToIfcOwnerHistoryOid;
	}
	private void setEmailToPersonAndOrganizationOid(Map<String,Long> emailToPersonAndOrganizationOid)
	{
		this.EmailToPersonAndOrganizationOid = emailToPersonAndOrganizationOid;
	}
	private void setEnumerationNameToOid(Map<String, Long> enumerationNameToOid)
	{
		this.enumerationNameToOid = enumerationNameToOid;
	}
	private void setFacilityNameToOid(Map<String, Long> facilityNameToOid)
	{
		FacilityNameToOid = facilityNameToOid;
	}
	private void setFloorNameToOid(Map<String, Long> floorNameToOid)
	{
		FloorNameToOid = floorNameToOid;
	}
	private void setFloorNameToSpaceNames(Map<String, ArrayList<String>> floorNameToSpaceNames)
	{
		FloorNameToSpaceNames = floorNameToSpaceNames;
	}
	private void setSpaceNameToComponentNames(Map<String, ArrayList<String>> spaceNameToComponentNames)
	{
		this.spaceNameToComponentNames = spaceNameToComponentNames;
	}
	private void setSpaceNameToOid(Map<String, Long> spaceNameToOid)
	{
		SpaceNameToOid = spaceNameToOid;
	}
	private void setTypeNameToComponentNames(Map<String, ArrayList<String>> typeNameToComponentNames)
	{
		this.typeNameToComponentNames = typeNameToComponentNames;
	}
	private void setTypeNameToOid(Map<String, Long> typeNameToOid)
	{
		TypeNameToOid = typeNameToOid;
	}
	private void setUnitNameToOid(Map<String, Long> unitNameToOid)
	{
		this.unitNameToOid = unitNameToOid;
	}
	private void setSystemNameCategoryToOid(Map<String,Long> systemNameCategoryToOid)
	{
		this.systemNameCategoryToOid = systemNameCategoryToOid;
	}

}
