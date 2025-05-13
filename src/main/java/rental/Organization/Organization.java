package rental.Organization;

public class Organization {
    private int organizationId;
    private String website;
    private String name;
    private String inn;

    public Organization(int organizationId, String website, String name, String inn) {
        this.organizationId = organizationId;
        this.website = website;
        this.name = name;
        this.inn = inn;
    }

    public int getOrganizationId() { return organizationId; }
    public void setOrganizationId(int organizationId) { this.organizationId = organizationId; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getInn() { return inn; }
    public void setInn(String inn) { this.inn = inn; }
}
