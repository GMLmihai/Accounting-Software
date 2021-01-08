package com.company.service;

import com.company.model.Employee;

import java.util.Comparator;

public class EmployeesDateComparator implements Comparator<Employee> {
    @Override
    public int compare(Employee o1, Employee o2) {
        return  o1.getEmploymentDate().compareTo(o2.getEmploymentDate());
    }
}
