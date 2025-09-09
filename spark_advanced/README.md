# Introduction

This project focuses on analyzing retail transaction data using Databricks and PySpark. The data comes from a CSV file and includes information about invoices, customers, and products. The goal is to clean and transform the data to find useful insights such as sales trends, active customers, and product performance.

# Technologies

- **PySpark**: 
    - Used for distributed data processing and transformation of large datasets.

- **Databricks**: 
    - Provided the collaborative and scalable environment to develop and run PySpark code efficiently.

- **Azure**: 
    - Hosted the Databricks workspace, enabling cloud-based storage and compute resources.

- **Spark**: 
    - The underlying engine powering PySpark, used for fast and scalable big data processing.

- **Spark SQL**: 
    - Used to write SQL queries within PySpark for efficient data filtering, aggregation, and analysis.

- **Matplotlib**: 
    - Used to create visualizations that helped communicate key insights such as sales trends and customer behavior.

# Databricks Implementation
- This project uses Databricks to analyze retail data and find useful insights like sales trends, active customers, and product performance. The data comes from one source: a CSV file. I used PySpark to clean, transform, and analyze the data. And used Pandas to create visual charts.

- Architecture 
    - DBFS (Databricks File System) stores the CSV file

    - Azure Storage is used for external data storage

    - Hive Metastore holds table info

    - PySpark handles data processing and analytics


# Future Improvement
- Integrate Real-Time Analytics: Extend the solution to support real-time data processing using Spark Structured Streaming, enabling up-to-date insights on customer activity and sales.

- Better Visualization and Reporting: Incorporate interactive dashboards using tools like Power BI or Tableau to make insights more accessible and actionable for non-technical stakeholders.

- Refactor the existing code for better results and performances.