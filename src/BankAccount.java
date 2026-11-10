import java.util.Objects;

public class BankAccount {

    private final String owner;
    private final Balance balance;

    private final long dailyWithdrawLimit;

    private final boolean isBlocked;

    public BankAccount(String owner, Balance initialBalance, long dailyWithdrawLimit) {
        this(owner, initialBalance, dailyWithdrawLimit, false);
    }

    public BankAccount(String owner, Balance initialBalance, long dailyWithdrawLimit, boolean isBlocked) {
        this.owner = Objects.requireNonNull(owner, "Owner cannot be null");
        this.balance = Objects.requireNonNull(initialBalance, "Initial balance cannot be null");

        if (dailyWithdrawLimit < 0) {
            throw new IllegalArgumentException("Withdraw limit cannot be negative");
        }
        this.dailyWithdrawLimit = dailyWithdrawLimit;
        this.isBlocked = isBlocked;
    }

    public String getOwner() {
        return owner;
    }

    public Balance getBalance() {
        return balance;
    }

    public long getDailyWithdrawLimit() {
        return dailyWithdrawLimit;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    private void validateWithdrawal(long amountKopecks) {
        if (isBlocked) {
            throw new IllegalStateException("Account is blocked");
        }
        if (amountKopecks > dailyWithdrawLimit) {
            throw new IllegalArgumentException("Amount exceeds daily withdraw limit of " + dailyWithdrawLimit + " kopecks");
        }
    }

    public BankAccount deposit(long amountKopecks) {
        Balance newBalance = balance.add(amountKopecks);
        return new BankAccount(owner, newBalance, dailyWithdrawLimit, isBlocked);
    }

    public BankAccount withdraw(long amountKopecks) {
        validateWithdrawal(amountKopecks);
        Balance newBalance = balance.subtract(amountKopecks);
        return new BankAccount(owner, newBalance, dailyWithdrawLimit, isBlocked);
    }

    public BankAccount transferTo(BankAccount target, long amountKopecks) {
        Objects.requireNonNull(target, "Target account cannot be null");

        validateWithdrawal(amountKopecks);
        if (target.isBlocked) {
            throw new IllegalStateException("Target account is blocked");
        }

        Balance newSenderBalance = this.balance.subtract(amountKopecks);

        Balance incomingBalance = new Balance(amountKopecks, this.balance.getCurrency())
                .convertTo(target.balance.getCurrency());

        Balance newReceiverBalance = target.balance.add(incomingBalance.getAmountInKopecks());

        return new BankAccount(this.owner, newSenderBalance, this.dailyWithdrawLimit, this.isBlocked);
    }

    public BankAccount receive(BankAccount sender, long amountKopecks) {
        Balance incomingBalance = new Balance(amountKopecks, sender.getBalance().getCurrency())
                .convertTo(this.balance.getCurrency());
        Balance newBalance = this.balance.add(incomingBalance.getAmountInKopecks());
        return new BankAccount(this.owner, newBalance, this.dailyWithdrawLimit, this.isBlocked);
    }

    @Override
    public String toString() {
        return String.format("BankAccount[%s: %s, limit: %d коп., blocked: %s]",
                owner, balance, dailyWithdrawLimit, isBlocked);
    }
}