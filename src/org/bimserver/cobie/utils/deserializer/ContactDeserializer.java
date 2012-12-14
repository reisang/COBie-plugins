package org.bimserver.cobie.utils.deserializer;
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

import java.util.Calendar;

import org.bimserver.cobie.cobielite.COBIEType;
import org.bimserver.cobie.cobielite.ContactType;
import org.bimserver.cobie.plugins.COBieIfcModel;
import org.bimserver.emf.OidProvider;
import org.bimserver.models.ifc2x3tc1.Ifc2x3tc1Factory;
import org.bimserver.models.ifc2x3tc1.IfcActorRole;
import org.bimserver.models.ifc2x3tc1.IfcOrganization;
import org.bimserver.models.ifc2x3tc1.IfcPerson;
import org.bimserver.models.ifc2x3tc1.IfcPersonAndOrganization;
import org.bimserver.models.ifc2x3tc1.IfcPostalAddress;
import org.bimserver.models.ifc2x3tc1.IfcRoleEnum;
import org.bimserver.models.ifc2x3tc1.IfcTelecomAddress;


public class ContactDeserializer 
{
	private COBIEType.Contacts contacts;
	private COBieIfcModel model;
	private OidProvider CobieOidProvider;
	private OwnerHistoryHandler ownerHistoryProvider;
	
	public ContactDeserializer(COBIEType.Contacts contacts, COBieIfcModel cobieIfcModel, 
			IfcCommonHandler ifcCommonHandler)
	{
		this.contacts = contacts;
		this.model = cobieIfcModel;
		ifcCommonHandler.getGuidHandler();
		this.CobieOidProvider = ifcCommonHandler.getOidProvider();
		this.ownerHistoryProvider = ifcCommonHandler.getOwnerHistoryHandler();
	}
	
	public void contactToPersonOrgAndOwnerHistory(ContactType contact) throws Exception
	{
		String email;
		String createdBy;
		Calendar createdOn;
		email = contact.getEmail();
		if (!model.containsContact(email))
		{
			createdBy = contact.getCreatedBy();
			createdOn = contact.getCreatedOn();
			IfcPerson person = ContactDeserializer.personFromContact(contact);
			IfcOrganization org = ContactDeserializer
					.organizationFromContact(contact);
			IfcTelecomAddress telecommAddress = ContactDeserializer
					.telecommAddressFromContact(contact);
			org.getAddresses().add(telecommAddress);
			IfcPersonAndOrganization personOrg = Ifc2x3tc1Factory.eINSTANCE
					.createIfcPersonAndOrganization();
			IfcActorRole actorRole = ContactDeserializer
					.actorRoleFromContact(contact);
			org.getRoles().add(actorRole);
			personOrg.setTheOrganization(org);
			personOrg.setThePerson(person);
			model.add(person, CobieOidProvider);
			model.add(telecommAddress, CobieOidProvider);
			model.add(actorRole, CobieOidProvider);
			model.add(org, CobieOidProvider);
			model.add(personOrg, CobieOidProvider);
			if (model.containsContact(createdBy) &&
					!model.containsCreatedBy(createdBy))
			{
				ownerHistoryProvider.ownerHistoryFromEmailAndTimestamp(createdBy, createdOn);
			}

		}
	}
	
	public void deserializeContacts()
	{
		try
		{
			if (contacts != null && contacts.getContactArray()!=null)
			{
				for (ContactType contact : contacts.getContactArray())
				{
					contactToPersonOrgAndOwnerHistory(contact);
				}
			}
		} catch (Exception ex)
		{
		}
	}

	public static IfcPerson personFromContact(ContactType contact)
	{
		IfcPerson tmpPerson =
				Ifc2x3tc1Factory.eINSTANCE.createIfcPerson();
		String GivenName=contact.getGivenName();
		String FamilyName=contact.getFamilyName();
		tmpPerson.setFamilyName(FamilyName);
		tmpPerson.setGivenName(GivenName);
		tmpPerson.setId(contact.getExternalIdentifier());
		return tmpPerson;
	}
	
	public static IfcOrganization organizationFromContact(ContactType contact)
	{
		IfcOrganization tmpOrg =
				Ifc2x3tc1Factory.eINSTANCE.createIfcOrganization();
		String Company=contact.getCompany();
		String Department=contact.getDepartment();
		String OrganizationCode=contact.getOrganizationCode();
		tmpOrg.setName(Company);
		tmpOrg.setId(OrganizationCode);
		tmpOrg.setDescription(Department);
		return tmpOrg;
	}
	
	public static IfcTelecomAddress telecommAddressFromContact(ContactType contact)
	{
		IfcTelecomAddress telecommAddress = 
				Ifc2x3tc1Factory.eINSTANCE.createIfcTelecomAddress();
		telecommAddress.getElectronicMailAddresses().add(contact.getEmail());
		telecommAddress.getTelephoneNumbers().add(contact.getPhone());
		//telecommAddress.
		return telecommAddress;
	}
	
	public static IfcPostalAddress postalAddressFromContact(ContactType contact)
	{
		IfcPostalAddress postalAddress =
				Ifc2x3tc1Factory.eINSTANCE.createIfcPostalAddress();
		postalAddress.getAddressLines().add(contact.getStreet());
		postalAddress.setPostalBox(contact.getPostalBox());
		postalAddress.setTown(contact.getTown());
		postalAddress.setRegion(contact.getStateRegion());
		postalAddress.setPostalCode(contact.getPostalCode());
		postalAddress.setCountry(contact.getCountry());
		return postalAddress;
		
	}
	
	public static IfcActorRole actorRoleFromContact(ContactType contact)
	{
		IfcActorRole role =
				Ifc2x3tc1Factory.eINSTANCE.createIfcActorRole();
		role.setRole(IfcRoleEnum.USERDEFINED);
		role.setUserDefinedRole(contact.getCategory());
		return role;
	}
	
}
