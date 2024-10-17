package com.argusoft.form.dto;

public class LocationDTO {

    private Long id;

    private String name;

    private Long parent_id;

    // private List<LocationDTO> children;

    private boolean havingChild;

    public LocationDTO() {
    }

    public LocationDTO(Long id, String name, Long parent_id, boolean havingChild) {
        this.id = id;
        this.name = name;
        this.parent_id = parent_id;
        // this.children = children;
        this.havingChild = havingChild;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    // public List<LocationDTO> getChildren() {
    // return children;
    // }

    // public void setChildren(List<LocationDTO> children) {
    // this.children = children;
    // }

    public boolean isHavingChild() {
        return havingChild;
    }

    public void setHavingChild(boolean havingChild) {
        this.havingChild = havingChild;
    }

    @Override
    public String toString() {
        return "LocationDTO [id=" + id + ", name=" + name + ", parent_id=" + parent_id + ", havingChild=" + havingChild
                + "]";
    }
}
