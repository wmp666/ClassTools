package com.wmp.PublicTools;

import java.util.ArrayList;
import java.util.List;

public class ExceptionStringConverter {

    /**
     * 将嵌套异常转换为完整的字符串（包含所有堆栈信息）
     */
    public static String convertToString(Throwable throwable) {
        return convertToString(throwable, true);
    }

    /**
     * 将嵌套异常转换为字符串
     *
     * @param throwable         异常对象
     * @param includeStackTrace 是否包含完整的堆栈信息
     */
    public static String convertToString(Throwable throwable, boolean includeStackTrace) {
        if (throwable == null) {
            return "null";
        }

        StringBuilder sb = new StringBuilder();
        convertExceptionChain(sb, throwable, 0, includeStackTrace, new ArrayList<>());
        return sb.toString();
    }

    /**
     * 转换为简洁的异常信息（只显示关键信息）
     */
    public static String convertToSimpleString(Throwable throwable) {
        if (throwable == null) {
            return "null";
        }

        StringBuilder sb = new StringBuilder();
        Throwable current = throwable;
        int level = 0;

        while (current != null) {
            if (level > 0) {
                sb.append("\nCaused by: ");
            }

            sb.append(current.getClass().getSimpleName())
                    .append(": ")
                    .append(current.getMessage());

            // 添加第一个堆栈位置
            if (current.getStackTrace().length > 0) {
                StackTraceElement first = current.getStackTrace()[0];
                sb.append(" (at ")
                        .append(first.getClassName())
                        .append(".")
                        .append(first.getMethodName())
                        .append(":")
                        .append(first.getLineNumber())
                        .append(")");
            }

            current = current.getCause();
            level++;

            // 防止无限循环
            if (level > 20) {
                sb.append("\n... (exception chain too long)");
                break;
            }
        }

        return sb.toString();
    }

    private static void convertExceptionChain(StringBuilder sb, Throwable throwable,
                                              int depth, boolean includeStackTrace,
                                              List<Throwable> processedExceptions) {
        if (throwable == null || processedExceptions.contains(throwable)) {
            return;
        }

        processedExceptions.add(throwable);

        String indent = getIndent(depth);

        // 异常头信息
        if (depth == 0) {
            sb.append("Exception Chain:\n");
        } else {
            sb.append(indent).append("Caused by: ");
        }

        sb.append(throwable.getClass().getName())
                .append(": ")
                .append(throwable.getMessage())
                .append("\n");

        // 堆栈信息
        if (includeStackTrace) {
            StackTraceElement[] stackTrace = throwable.getStackTrace();
            for (StackTraceElement element : stackTrace) {
                sb.append(indent).append("    at ")
                        .append(element.getClassName())
                        .append(".")
                        .append(element.getMethodName())
                        .append("(")
                        .append(element.getFileName())
                        .append(":")
                        .append(element.getLineNumber())
                        .append(")\n");
            }
        } else {
            // 只显示第一个堆栈位置
            if (throwable.getStackTrace().length > 0) {
                StackTraceElement first = throwable.getStackTrace()[0];
                sb.append(indent).append("    at ")
                        .append(first.getClassName())
                        .append(".")
                        .append(first.getMethodName())
                        .append("(")
                        .append(first.getFileName())
                        .append(":")
                        .append(first.getLineNumber())
                        .append(")\n");
            }
        }

        // 处理 suppressed exceptions (try-with-resources)
        Throwable[] suppressed = throwable.getSuppressed();
        if (suppressed.length > 0) {
            sb.append(indent).append("Suppressed exceptions:\n");
            for (Throwable suppressedEx : suppressed) {
                convertExceptionChain(sb, suppressedEx, depth + 1, includeStackTrace, processedExceptions);
            }
        }

        // 处理原因异常
        Throwable cause = throwable.getCause();
        if (cause != null) {
            convertExceptionChain(sb, cause, depth + 1, includeStackTrace, processedExceptions);
        }
    }

    private static String getIndent(int depth) {
        return "    ".repeat(depth);
    }

    /**
     * 获取异常链的统计信息
     */
    public static String getExceptionChainSummary(Throwable throwable) {
        if (throwable == null) {
            return "No exception";
        }

        StringBuilder sb = new StringBuilder();
        List<Class<?>> exceptionTypes = new ArrayList<>();
        List<String> exceptionMessages = new ArrayList<>();

        Throwable current = throwable;
        int chainLength = 0;

        while (current != null) {
            exceptionTypes.add(current.getClass());
            exceptionMessages.add(current.getMessage());
            current = current.getCause();
            chainLength++;

            if (chainLength > 50) { // 防止无限循环
                break;
            }
        }

        sb.append("Exception Chain Summary:\n");
        sb.append("Total exceptions in chain: ").append(chainLength).append("\n");
        sb.append("Exception types:\n");

        for (int i = 0; i < exceptionTypes.size(); i++) {
            sb.append("  ").append(i + 1).append(". ")
                    .append(exceptionTypes.get(i).getSimpleName())
                    .append(": ")
                    .append(exceptionMessages.get(i))
                    .append("\n");
        }

        return sb.toString();
    }
}