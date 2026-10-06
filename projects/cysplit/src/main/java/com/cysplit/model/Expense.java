package com.cysplit.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "payer_id")
    private User payer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Split> splits = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();

    public Expense() {}

    public Expense(String description, BigDecimal totalAmount, User payer, Group group) {
        this.description = description;
        this.totalAmount = totalAmount;
        this.payer = payer;
        this.group = group;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public User getPayer() { return payer; }
    public void setPayer(User payer) { this.payer = payer; }

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }

    public List<Split> getSplits() { return splits; }
    public void setSplits(List<Split> splits) { this.splits = splits; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public void addSplit(Split split) {
        split.setExpense(this);
        this.splits.add(split);
    }
}
