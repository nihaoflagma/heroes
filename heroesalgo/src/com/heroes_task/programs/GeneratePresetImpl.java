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

    // Сложность:
    // Сортировка - O(n log n)
    // Выбор юнитов - O(n)
    // В итоге ~ O(n log n)
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

    // Сортировка юнитов по их эффективности
    private void sortUnitsByEffectiveness(List<Unit> units) {
        units.sort(Comparator.comparingDouble(unit -> -((double) (unit.getBaseAttack() + unit.getHealth()) / unit.getCost())));
    }

    // Вычисляем максимальное количество юнитов, которые можно добавить
    private int calculateMaxUnitsToAdd(Unit unit, int maxPoints, int currentPoints) {
        return Math.min(11, (maxPoints - currentPoints) / unit.getCost());
    }

    // Добавляем юниты в армию
    private void addUnitsToArmy(Unit unit, int unitsToAdd, List<Unit> selectedUnits) {
        for (int i = 0; i < unitsToAdd; i++) {
            Unit newUnit = createNewUnit(unit, i);
            selectedUnits.add(newUnit);
        }
    }

    // Новый юнит с уникальным именем
    private Unit createNewUnit(Unit unit, int index) {
        Unit newUnit = new Unit(unit.getName(), unit.getUnitType(), unit.getHealth(),
            unit.getBaseAttack(), unit.getCost(), unit.getAttackType(),
            unit.getAttackBonuses(), unit.getDefenceBonuses(), -1, -1);
        newUnit.setName(unit.getUnitType() + " " + index);
        return newUnit;
    }

    // Случайные координаты юнитам
    private void assignCoordinates(List<Unit> units) {
        Set<String> occupiedCoords = new HashSet<>();
        Random random = new Random();

        for (Unit unit : units) {
            assignRandomCoordinates(unit, occupiedCoords, random);
        }
    }

    // Присвоение юниту случайных координат и проверка на занятость
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
