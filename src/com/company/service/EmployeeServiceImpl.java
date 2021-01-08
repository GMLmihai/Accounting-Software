package com.company.service;

import com.company.mapper.EmployeeMapper;
import com.company.model.Department;
import com.company.model.Employee;
import com.company.repository.EmployeeRepository;
import com.company.repository.EmployeeRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();
    private EmployeeMapper employeeMapper = new EmployeeMapper();


    @Override
    public void listEmployees() {
        List<String> csvLines = employeeRepository.readCsv();
        for (String csvLine : csvLines) {
            Employee employee = employeeMapper.getEmployeeFromCsvLine(csvLine);
            if (employee != null) {
                System.out.println(employee.toString());
            }
        }
    }

    @Override
    public void addEmployee(Employee employee) {
        employee.setId(generateIdForNewEmployee());
        String employeeString = employeeMapper.getCsvLineEmployee(employee);
        employeeRepository.insertLine(employeeString);

    }

    @Override
    public void deleteEmployee(Integer id) {
        List<Employee> employeeList = employeeMapper.getEmployeesList(employeeRepository.readCsv());
        boolean find = false;
        for (Employee employee : employeeList) {
            if (employee.getId().equals(id)) {
                find = true;
                break;
            }
        }
        if (find) {
            employeeRepository.deleteLine(id);
            System.out.println("Employee was deleted successfully!");
        } else {
            System.out.println("Employee not exist in DB!");
        }

    }


    @Override
    public void updateEmployee(Employee employee) {
        String employeeString = employeeMapper.getCsvLineEmployee(employee);
        employeeRepository.updateLine(employee.getId(), employeeString);

    }

    @Override
    public Double calculateAverageSalary() {
        List<Employee> employees = employeeMapper.getEmployeesList(employeeRepository.readCsv());
        return calculateAverageSalary(employees);
    }

    @Override
    public Double calculateAverageSalaryByDepartment(Department department) {
        List<Employee> employees = employeeMapper.getEmployeesList(employeeRepository.readCsv());
        List<Employee> filteredEmployees = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getDepartment().equals(department)) {
                filteredEmployees.add(employee);
            }
        }
        return calculateAverageSalary(filteredEmployees);
    }


    private Double calculateAverageSalary(List<Employee> employees) {

        Double salarySum = 0.0;
        for (Employee employee : employees) {
            salarySum += employee.getSalary();
        }
        if (employees.size() < 1) {
            return 0.0;
        }
        return salarySum / employees.size();
    }

    @Override
    public void employeeDateComparator() {
        List<Employee> employees = employeeMapper.getEmployeesList(employeeRepository.readCsv());
        employees.sort(new EmployeesDateComparator());
        for (int i = 0; i < 10; i++) {
            System.out.println(employees.get(i).getName() + " " + employees.get(i).getEmploymentDate());
        }

    }

    @Override
    public void employeeSalaryComparator() {
        List<Employee> employees = employeeMapper.getEmployeesList(employeeRepository.readCsv());
        employees.sort(new EmployeesSalaryComparator());
        for (int i = 0; i < 10; i++) {
            System.out.println(employees.get(i).getName() + " " + employees.get(i).getSalary());
        }
    }

    @Override
    public Integer generateIdForNewEmployee() {
        List<Employee> employees = employeeMapper.getEmployeesList(employeeRepository.readCsv());
        Integer id = 0;
        for (Employee employee : employees) {
            id = employee.getId();
        }
        return id + 1;
    }
}


