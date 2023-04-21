package com.kapas.workorder.util;

import com.google.gson.Gson;
import com.kapas.workorder.model.MetaDataField;
import com.kapas.workorder.model.ParsedWorkflow;
import com.kapas.workorder.model.WorkflowTask;

import java.util.ArrayList;
import java.util.List;

public class GenerateWorkflowJSON {

    public static void main(String args[]) {

        ParsedWorkflow workflow = new ParsedWorkflow();

        workflow.setWorkflowId("SHRI_BALAJI_AGR_PROCUREMENT_PROCESS");
        workflow.setTotalTask(4);
        List<WorkflowTask> taskList = new ArrayList<>(4);

        WorkflowTask task1 = new WorkflowTask();
        task1.setTaskId("GENERATE_TOKEN");
        task1.setTaskNumber(1);
        task1.setTaskName("Generate Token");
        task1.setDescription("This task is assigned to user to insert vendor/vehicle details and generate token.");
        task1.setIsRequired(true);
        List<MetaDataField> fields1 = new ArrayList<>();
        fields1.add(new MetaDataField("vendor_fname","string",true));
        fields1.add(new MetaDataField("vendor_lname","string",true));
        fields1.add(new MetaDataField("vendor_address","string",true));
        fields1.add(new MetaDataField("vendor_state","string",true));
        fields1.add(new MetaDataField("vendor_city","string",true));
        fields1.add(new MetaDataField("vendor_mobile","string",true));
        fields1.add(new MetaDataField("vendor_id_type","string",true));
        fields1.add(new MetaDataField("vendor_id","string",true));
        fields1.add(new MetaDataField("vendor_vehicle","string",true));
        fields1.add(new MetaDataField("vendor_img","string",true));
        fields1.add(new MetaDataField("vendor_vehicle_img","string",true));
        task1.setFields(fields1);

        WorkflowTask task2 = new WorkflowTask();
        task2.setTaskId("GRADING");
        task2.setTaskNumber(2);
        task2.setTaskName("Sampling and Grading");
        task2.setDescription("This task is assigned to user to decide the cotton sample grade and insert grade/price details.");
        task2.setIsRequired(true);
        List<MetaDataField> fields2 = new ArrayList<>();
        fields2.add(new MetaDataField("grade","string",true));
        fields2.add(new MetaDataField("price","double",true));
        fields2.add(new MetaDataField("price_unit","string",true));
        task2.setFields(fields2);

        WorkflowTask task3 = new WorkflowTask();
        task3.setTaskId("WEIGHING");
        task3.setTaskNumber(3);
        task3.setTaskName("Cotton Weighing");
        task3.setDescription("This task is assigned to user to insert IN and OUT weight of cotton carrying vehicle.");
        task3.setIsRequired(true);
        List<MetaDataField> fields3 = new ArrayList<>();
        fields3.add(new MetaDataField("in_weight","double",true));
        fields3.add(new MetaDataField("out_weight","double",true));
        fields3.add(new MetaDataField("weigh_unit","string",true));
        task3.setFields(fields3);

        WorkflowTask task4 = new WorkflowTask();
        task4.setTaskId("WEIGHING");
        task4.setTaskNumber(4);
        task4.setTaskName("Cotton Weighing");
        task4.setDescription("This task is assigned to user to insert IN and OUT weight of cotton carrying vehicle.");
        task4.setIsRequired(true);
        List<MetaDataField> fields4 = new ArrayList<>();
        fields4.add(new MetaDataField("in_weight","double",true));
        fields4.add(new MetaDataField("out_weight","double",true));
        fields4.add(new MetaDataField("weigh_unit","string",true));
        task4.setFields(fields4);

        taskList.add(task1);
        taskList.add(task2);
        taskList.add(task3);
        taskList.add(task4);

        workflow.setTaskList(taskList);

        String workflowJSON = new Gson().toJson(workflow);

        System.out.println(workflowJSON);

        ParsedWorkflow workflow2 = new Gson().fromJson(workflowJSON, ParsedWorkflow.class);
        System.out.println(workflow2.toString());


    }

}
