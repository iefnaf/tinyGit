package gitlet;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 * @author gfanfei@gmail.com
 * @date 2022/5/6 20:36
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            Utils.exitWithError("Please enter a command.");
        }
        String firstArg = args[0];
        if (!firstArg.equals("init")) {
            // commands except the init command require being
            // in an initialized Gitlet working directory
            if (!Repository.initCheck()) {
                Utils.exitWithError("Not in an initialized Gitlet directory.");
            }
        }

        switch (firstArg) {
            case "init":
                validateNumArgs(args, 1);
                Repository.handleInit();
                break;
            case "add":
                validateNumArgs(args, 2);
                Repository.handleAdd(args[1]);
                break;
            case "commit":
                validateNumArgs(args, 2);
                Repository.handleCommit(args[1]);
                break;
            case "checkout":
                Repository.handleCheckout(args);
                break;
            case "log":
                validateNumArgs(args, 1);
                Repository.handleLog();
                break;
            case "global-log":
                validateNumArgs(args, 1);
                Repository.handleGlobalLog();
                break;
            case "find":
                validateNumArgs(args, 2);
                Repository.handleFind(args[1]);
                break;
            case "status":
                validateNumArgs(args, 1);
                Repository.handleStatus();
                break;
            case "rm":
                validateNumArgs(args, 2);
                Repository.handleRm(args[1]);
                break;
            case "branch":
                validateNumArgs(args, 2);
                Repository.handleBranch(args[1]);
                break;
            case "rm-branch":
                validateNumArgs(args, 2);
                Repository.handleRmBranch(args[1]);
                break;
            case "reset":
                validateNumArgs(args, 2);
                Repository.handleReset(args[1]);
                break;
            case "merge":
                validateNumArgs(args, 2);
                Repository.handleMerge(args[1]);
                break;
            default:
                Utils.exitWithError("No command with that name exists.");
                break;
        }
    }

    public static void validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            Utils.exitWithError("Incorrect operands.");
        }
    }
}
