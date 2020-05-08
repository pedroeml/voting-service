package com.voting.associado.model;

public enum VoteStatusEnum {
    ABLE("ABLE_TO_VOTE", true),
    UNABLE("UNABLE_TO_VOTE", false);

    private final String status;
    private final boolean canVote;

    VoteStatusEnum(String status, boolean canVote) {
        this.status = status;
        this.canVote = canVote;
    }

    public String getStatus() {
        return this.status;
    }

    public boolean getCanVote() {
        return this.canVote;
    }
}
