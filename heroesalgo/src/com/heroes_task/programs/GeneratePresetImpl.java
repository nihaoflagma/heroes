package com.heroes_task.programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GeneratePresetImpl implements GeneratePreset {

    
    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        Army computerArmy = new Army();
        List<Unit> selectedUnits = new ArrayList<>();

        sortUnitsByEffectiveness(unitList);

        int currentPoints = 0;

        for (Unit unit : unitList) {
            int unitsToAdd = calculateMaxUnitsToAdd(unit, maxPoints, currentPoints);
            addUnitsToArmy(unit, unitsToAdd, selectedUnits);
            currentPoints += unitsToAdd * unit.getCost();
        }

        assignCoordinates(selectedUnits);
        computerArmy.setUnits(selectedUnits);
        computerArmy.setPoints(currentPoints);
        return computerArmy;
    }

    
    private void sortUnitsByEffectiveness(List<Unit> units) {
        units.sort(Comparator.comparingDouble(unit -> -((double) (unit.getBaseAttack() + unit.getHealth()) / unit.getCost())));
    }

    
    private int calculateMaxUnitsToAdd(Unit unit, int maxPoints, int currentPoints) {
        return Math.min(11, (maxPoints - currentPoints) / unit.getCost());
    }

    
    private void addUnitsToArmy(Unit unit, int unitsToAdd, List<Unit> selectedUnits) {
        for (int i = 0; i < unitsToAdd; i++) {
            Unit newUnit = createNewUnit(unit, i);
            selectedUnits.add(newUnit);
        }
    }

   
    private Unit createNewUnit(Unit unit, int index) {
        Unit newUnit = new Unit(unit.getName(), unit.getUnitType(), unit.getHealth(),
            unit.getBaseAttack(), unit.getCost(), unit.getAttackType(),
            unit.getAttackBonuses(), unit.getDefenceBonuses(), -1, -1);
        newUnit.setName(unit.getUnitType() + " " + index);
        return newUnit;
    }

    
    private void assignCoordinates(List<Unit> units) {
        Set<String> occupiedCoords = new HashSet<>();
        Random random = new Random();

        for (Unit unit : units) {
            assignRandomCoordinates(unit, occupiedCoords, random);
        }
    }

    
    private void assignRandomCoordinates(Unit unit, Set<String> occupiedCoords, Random random) {
        int coordX, coordY;
        do {
            coordX = random.nextInt(3);
            coordY = random.nextInt(21);
        } while (occupiedCoords.contains(coordX + "," + coordY));
        occupiedCoords.add(coordX + "," + coordY);
        unit.setxCoordinate(coordX);
        unit.setyCoordinate(coordY);
    }
}
