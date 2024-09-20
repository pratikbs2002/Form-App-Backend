package com.argusoft.form.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "schema_mapping_table")
public class Schema {   

    @Id
    private String uuidName;
    private String schemaName;
    @CreationTimestamp
    private Date created_at;

    public Schema() {   
    }

    public Schema(String uuid_name, String schema_name, Date created_at) {
        this.uuidName = uuid_name;
        this.schemaName = schema_name;
        this.created_at = created_at;
    }

    

    public Schema(String uuidName, String schemaName) {
        this.uuidName = uuidName;
        this.schemaName = schemaName;
    }

    public String getUuidName() {
        return uuidName;
    }

    public void setUuidName(String uuid_name) {
        this.uuidName = uuid_name;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schema_name) {
        this.schemaName = schema_name;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Schema [uuid_name=" + uuidName + ", schema_name=" + schemaName + ", created_at=" + created_at + "]";
    }

}
