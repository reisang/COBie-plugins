package org.bimserver.cobie.utils.spreadsheetml;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

import nl.fountain.xelem.excel.Row;
import nl.fountain.xelem.excel.Workbook;
import nl.fountain.xelem.excel.Worksheet;

import org.bimserver.cobie.cobielite.COBIEType;
import org.bimserver.cobie.cobielite.FloorType;
import org.bimserver.cobie.utils.COBieUtility;


public class SpreadsheetToFloors {
	public static ArrayList<String> FloorColumnNames = new ArrayList<String>(
			Arrays.asList("Name", "CreatedBy", "CreatedOn", "Category",
					"ExtSystem", "ExtObject", "ExtIdentifier", "Description",
					"Elevation", "Height"));

	public static enum FloorColumnNameLiterals {
		Name, CreatedBy, CreatedOn, Category, ExtSystem, ExtObject, ExtIdentifier, Description, Elevation, Height
	};

	public static ArrayList<String> getFloorColumns()
	{
		return FloorColumnNames;
	}

	public static void writeFloorsToCOBie(COBIEType cType, Workbook workbook) {
		COBIEType.Floors floors = cType.addNewFloors();

		Worksheet sheet = workbook
				.getWorksheet(COBieUtility.CobieSheetName.Floor.toString());
		Map<String, Integer> columnDictionary = COBieUtility
				.GetWorksheetColumnDictionary(sheet, getFloorColumns());
		String floorName = "";
		String floorCreatedBy = "";
		String floorCreatedOnString = "";
		Calendar floorCreatedOn;
		String floorCategory = "";
		String floorExtSystem = "";
		String floorExtObject = "";
		String floorExtIdentifier = "";
		String floorDescription = "";
		String floorElevation = "";
		String floorHeight = "";

		int idxName;
		int idxCreatedBy;
		int idxCreatedOn;
		int idxCategory;
		int idxExtSystem;
		int idxExtObject;
		int idxExtIdentifier;
		int idxDescription;
		int idxElevation;
		int idxHeight;

		idxName = columnDictionary.get(FloorColumnNameLiterals.Name.toString());
		idxCreatedBy = columnDictionary.get(FloorColumnNameLiterals.CreatedBy
				.toString());
		idxCreatedOn = columnDictionary.get(FloorColumnNameLiterals.CreatedOn
				.toString());
		idxCategory = columnDictionary.get(FloorColumnNameLiterals.Category
				.toString());
		idxExtSystem = columnDictionary.get(FloorColumnNameLiterals.ExtSystem
				.toString());
		idxExtObject = columnDictionary.get(FloorColumnNameLiterals.ExtObject
				.toString());
		idxExtIdentifier = columnDictionary
				.get(FloorColumnNameLiterals.ExtIdentifier.toString());
		idxDescription = columnDictionary
				.get(FloorColumnNameLiterals.Description.toString());
		idxElevation = columnDictionary.get(FloorColumnNameLiterals.Elevation
				.toString());
		idxHeight = columnDictionary.get(FloorColumnNameLiterals.Height
				.toString());
		int rowIdx;
		int firstRowIdx = Worksheet.firstRow;
		for (Row rowData : sheet.getRows()) {
			rowIdx = rowData.getIndex();
			if (rowIdx > firstRowIdx && COBieSpreadSheet.isRowPopulated(rowData, 1,100)) {
				FloorType tmpFloor = floors.addNewFloor();
				if (idxName > -1)
					floorName = rowData.getCellAt(idxName).getData$();
				if (idxCreatedBy > -1)
					floorCreatedBy = rowData.getCellAt(idxCreatedBy).getData$();
				if (idxCreatedOn > -1)
					floorCreatedOnString = rowData.getCellAt(idxCreatedOn)
							.getData$();
				if (idxCategory > -1)
					floorCategory = rowData.getCellAt(idxCategory).getData$();
				if (idxExtSystem > -1)
					floorExtSystem = rowData.getCellAt(idxExtSystem).getData$();
				if (idxExtObject > -1)
					floorExtObject = rowData.getCellAt(idxExtObject).getData$();
				if (idxExtIdentifier > -1)
					floorExtIdentifier = rowData.getCellAt(idxExtIdentifier)
							.getData$();
				if (idxDescription > -1)
					floorDescription = rowData.getCellAt(idxDescription)
							.getData$();
				if (idxElevation > -1)
					floorElevation = rowData.getCellAt(idxElevation).getData$();
				if (idxHeight > -1)
					floorHeight = rowData.getCellAt(idxHeight).getData$();
				floorCreatedOn = COBieUtility
						.calendarFromString(floorCreatedOnString);
				tmpFloor.setName(COBieUtility.getCOBieString(floorName));
				tmpFloor.setCreatedBy(COBieUtility
						.getCOBieString(floorCreatedBy));
				tmpFloor.setCreatedOn(floorCreatedOn);
				tmpFloor.setCategory(COBieUtility.getCOBieString(floorCategory));
				tmpFloor.setExtSystem(COBieUtility
						.getCOBieString(floorExtSystem));
				tmpFloor.setExtObject(COBieUtility
						.getCOBieString(floorExtObject));
				tmpFloor.setExtIdentifier(COBieUtility
						.getCOBieString(floorExtIdentifier));
				tmpFloor.setDescription(COBieUtility
						.getCOBieString(floorDescription));
				tmpFloor.setElevation(COBieUtility
						.getCOBieString(floorElevation));
				tmpFloor.setHeight(COBieUtility.getCOBieString(floorHeight));
			}
		}

	}



	
}
