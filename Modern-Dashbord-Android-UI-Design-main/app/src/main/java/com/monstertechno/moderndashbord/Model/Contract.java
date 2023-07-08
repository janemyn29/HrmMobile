package com.monstertechno.moderndashbord.Model;

import java.util.Date;

public class Contract {

        public String username;
        public String id;
        public String contractCode;
        public String file;
        public Date startDate;
        public Date endDate;
        public String job;
        public int basicSalary;
        public String status;
        public int percentDeduction;
        public String salaryType;
        public String contractType;
        public boolean isPersonalTaxDeduction;
        public String insuranceType;
        public int insuranceAmount;
        public ApplicationUser applicationUser;

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        public String getContractCode() {
                return contractCode;
        }

        public void setContractCode(String contractCode) {
                this.contractCode = contractCode;
        }

        public String getFile() {
                return file;
        }

        public void setFile(String file) {
                this.file = file;
        }

        public Date getStartDate() {
                return startDate;
        }

        public void setStartDate(Date startDate) {
                this.startDate = startDate;
        }

        public Date getEndDate() {
                return endDate;
        }

        public void setEndDate(Date endDate) {
                this.endDate = endDate;
        }

        public String getJob() {
                return job;
        }

        public void setJob(String job) {
                this.job = job;
        }

        public int getBasicSalary() {
                return basicSalary;
        }

        public void setBasicSalary(int basicSalary) {
                this.basicSalary = basicSalary;
        }

        public String getStatus() {
                return status;
        }

        public void setStatus(String status) {
                this.status = status;
        }

        public int getPercentDeduction() {
                return percentDeduction;
        }

        public void setPercentDeduction(int percentDeduction) {
                this.percentDeduction = percentDeduction;
        }

        public String getSalaryType() {
                return salaryType;
        }

        public void setSalaryType(String salaryType) {
                this.salaryType = salaryType;
        }

        public String getContractType() {
                return contractType;
        }

        public void setContractType(String contractType) {
                this.contractType = contractType;
        }

        public boolean isPersonalTaxDeduction() {
                return isPersonalTaxDeduction;
        }

        public void setPersonalTaxDeduction(boolean personalTaxDeduction) {
                isPersonalTaxDeduction = personalTaxDeduction;
        }

        public String getInsuranceType() {
                return insuranceType;
        }

        public void setInsuranceType(String insuranceType) {
                this.insuranceType = insuranceType;
        }

        public int getInsuranceAmount() {
                return insuranceAmount;
        }

        public void setInsuranceAmount(int insuranceAmount) {
                this.insuranceAmount = insuranceAmount;
        }

        public ApplicationUser getApplicationUser() {
                return applicationUser;
        }

        public void setApplicationUser(ApplicationUser applicationUser) {
                this.applicationUser = applicationUser;
        }
}
