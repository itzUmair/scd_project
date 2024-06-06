package app.models;

public class PartyCandidate {
    private String cnic;
    public String name;
    public String logo;
    public String party_name;

    public int votes;

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPartyName() {
        return party_name;
    }

    public void setParty_name(String party_name) {
        this.party_name = party_name;
    }

    public PartyCandidate(String cnic, String name, String logo, String party_name) {
        this.cnic = cnic;
        this.name = name;
        this.logo = logo;
        this.party_name = party_name;
    }
}
