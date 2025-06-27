    public static String[] showConfirmInputDialog(Container owner, String title, String message, Icon icon, boolean isAlwaysOnTop, String... choices) {
        Object[] o = showDefaultDialog(owner, title, message, icon, INFORMATION_MESSAGE, YES_NO_BUTTONS, MESSAGE_INPUT, isAlwaysOnTop, choices);
        if (o != null) {
            String[] result = new String[o.length];
            for (int i = 0; i < o.length; i++) {
                result[i] = o[i].toString();
            }
            return result;
        }
        return new String[]{"NULL", ""};
    }