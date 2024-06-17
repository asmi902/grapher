import java.util.*;

public class Utils
{
    public static String validator(String s) {
        String parenthesis = "";
        int i = 0;

        StringBuffer str = new StringBuffer(s);

        if (str.charAt(0) == ')' || str.charAt(0) == '(') {
            parenthesis += str.charAt(0);
        }


        while (i < str.length()-1){
            // System.out.println("both: " + str.charAt(i) + " " + i);
            char a = str.charAt(i);
            char b = str.charAt(i+1);
            
            if (b == '(' || b ==')') {
                parenthesis += b;
            }

            boolean twoVars = (a == 'x' && b == 'x');
            boolean digitAndVar = (Character.isDigit(a) && b == 'x');
            boolean bracketAndVar = (a == ')' && (b == 'x' || Character.isDigit(b)));
            boolean varAndBracket =  (Character.isDigit(a) || a == 'x') && b == '(';

            if (twoVars || digitAndVar || bracketAndVar || varAndBracket) {
                str.insert(i+1, "*");

                i += 1;
            } else {
                    
                boolean leftIsOperator = !(Character.isDigit(a) || a == 'x' || a == ')');
                boolean rightIsOperator = !(Character.isDigit(b) || b == 'x' || b == '(');
                
                if (leftIsOperator && rightIsOperator) {
                    return "";
                }
            }
            
            i += 1;
        } 
        // System.out.println(parenthesis);
        if (checkParenthesis(parenthesis)) {
            // System.out.println(str.toString());
            return str.toString();        
        }
        return "";
    }


    static boolean checkParenthesis(String paren) {

        if (paren.isEmpty()) {
            return true;
        } else {

            Stack<Character> stack = new Stack<>();

            for (int i = 0; i < paren.length(); i++) {
                char current = paren.charAt(i);

                if (current == '(' || current == '[' || current == '{') {
                    stack.push(current);
                } else {

                    if(stack.isEmpty()) {
                          return false;
                    }
                    char peekChar = stack.peek();

                    if ((current == ')' && peekChar != '(')
                            || (current == '}' && peekChar != '{')
                            || (current == ']' && peekChar != '[')) {
                        return false;  
                    } else {
                        stack.pop();
                    }
                }
            }
            return true; 
        }
    }


    static class nodePointer
    {
        String data;
        nodePointer left, right;
    } ;

    static nodePointer newNode(String c)
    {
        nodePointer n = new nodePointer();
        n.data = c;
        n.left = n.right = null;
        return n;
    }

    static nodePointer build(String s)
    {

        Stack<nodePointer> stN = new Stack<>();

        Stack<Character> stC = new Stack<>();
        nodePointer t, t1, t2;

        int []p = new int[123];
        p['+'] = p['-'] = 1;
        p['/'] = p['*'] = 2;
        p['^'] = 3;
        p[')'] = 0;

        int i = 0;

        while (i < s.length())
        {
            if (s.charAt(i) == '(') {

                stC.add(s.charAt(i));
                i += 1;
            } else if (Character.isAlphabetic(s.charAt(i)) || Character.isDigit(s.charAt(i)))
            {
                if (Character.isAlphabetic(s.charAt(i))) {
                    t = newNode(String.valueOf(s.charAt(i)));
                    stN.add(t);
                    i += 1;
                } else {
                    int j = i;
                    String num = "";
                    while (j < s.length()) {
                        if (Character.isDigit(s.charAt(j))) {
                            num += String.valueOf(s.charAt(j));
                            j += 1;
                        } else {
                            break;
                        }
                    }

                    t = newNode(num);
                    stN.add(t);

                    i = j;
                }
            } else if (p[s.charAt(i)] > 0)
            {
                while (
                        !stC.isEmpty() && stC.peek() != '('
                                && ((s.charAt(i) != '^' && p[stC.peek()] >= p[s.charAt(i)])
                                || (s.charAt(i) == '^'
                                && p[stC.peek()] > p[s.charAt(i)])))
                {
                    t = newNode(String.valueOf(stC.peek()));
                    stC.pop();

                    t1 = stN.peek();
                    stN.pop();

                    t2 = stN.peek();
                    stN.pop();

                    t.left = t2;
                    t.right = t1;

                    stN.add(t);
                }

                stC.push(s.charAt(i));
                i += 1;
            }
            else if (s.charAt(i) == ')') {
                while (!stC.isEmpty() && stC.peek() != '(')
                {
                    t = newNode(String.valueOf(stC.peek()));
                    stC.pop();
                    t1 = stN.peek();
                    stN.pop();
                    t2 = stN.peek();
                    stN.pop();
                    t.left = t2;
                    t.right = t1;
                    stN.add(t);
                }
                stC.pop();
                i += 1;
            }
        }
        t = stN.peek();
        return t;
    }
    static void postorder(nodePointer root)
    {
        if (root != null)
        {
            postorder(root.left);
            postorder(root.right);
            System.out.print(root.data);
        }
    }

    private static boolean isNumeric(String n) {
        try {
            Integer.parseInt(n);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static double getFunction(double x, nodePointer root) {
        if (root.right == null && root.left == null) {
            if (isNumeric(root.data)) {
                return Double.parseDouble(root.data);
            } else {
                // CAN ONLY BE NUMBER OR X
                return x;
            }
        } 

        boolean isLeftDigit = isNumeric(root.left.data);
        boolean isRightDigit = isNumeric(root.right.data);

        if (isLeftDigit && isRightDigit) {
            double a = Integer.parseInt(String.valueOf(root.left.data));
            double b = Integer.parseInt(String.valueOf(root.right.data));
            switch (root.data) {
                case "+":
                    return Solve.add(a, b);
                case "-":
                    return Solve.subtract(a, b);
                case "*":
                    return Solve.multiply(a, b);
                case "/":
                    return Solve.divide(a, b);
                case "^":
                    return Solve.exp(a, b);
            }
        } else if (isLeftDigit) {
            double a = Integer.parseInt(String.valueOf(root.left.data));
            if (Objects.equals(root.right.data, "x")) {
                switch (root.data) {
                    case "+":
                        return Solve.add(a, x);
                    case "-":
                        return Solve.subtract(a, x);
                    case "*":
                        return Solve.multiply(a, x);
                    case "/":
                        return Solve.divide(a, x);
                    case "^":
                        return Solve.exp(a, x);
                }
            } else {
                switch (root.data) {
                    case "+":
                        return Solve.add(a, getFunction(x, root.right));
                    case "-":
                        return Solve.subtract(a, getFunction(x, root.right));
                    case "*":
                        return Solve.multiply(a, getFunction(x, root.right));
                    case "/":
                        return Solve.divide(a, getFunction(x, root.right));
                    case "^":
                        return Solve.exp(a, getFunction(x, root.right));
                }
            }
        } else if (isRightDigit) {
            double b = Integer.parseInt(String.valueOf(root.right.data));
            if (Objects.equals(root.left.data, "x")) {
                switch (root.data) {
                    case "+":
                        return Solve.add(x, b);
                    case "-":
                        return Solve.subtract(x, b);
                    case "*":
                        return Solve.multiply(x, b);
                    case "/":
                        return Solve.divide(x, b);
                    case "^":
                        return Solve.exp(x, b);
                }
            } else {
                switch (root.data) {
                    case "+":
                        return Solve.add(getFunction(x, root.left), b);
                    case "-":
                        return Solve.subtract(getFunction(x, root.left), b);
                    case "*":
                        return Solve.multiply(getFunction(x, root.left), b);
                    case "/":
                        return Solve.divide(getFunction(x, root.left), b);
                    case "^":
                        return Solve.exp(getFunction(x, root.left), b);
                }
            }
        } else {
            if (Objects.equals(root.left.data, "x") && Objects.equals(root.right.data, "x")) {
                switch (root.data) {
                    case "+":
                        return Solve.add(x, x);
                    case "-":
                        return Solve.subtract(x, x);
                    case "*":
                        return Solve.multiply(x, x);
                    case "/":
                        return Solve.divide(x, x);
                    case "^":
                        return Solve.exp(x, x);
                }
            } else if (Objects.equals(root.left.data, "x")) {
                switch (root.data) {
                    case "+":
                        return Solve.add(x, getFunction(x, root.right));
                    case "-":
                        return Solve.subtract(x, getFunction(x, root.right));
                    case "*":
                        return Solve.multiply(x, getFunction(x, root.right));
                    case "/":
                        return Solve.divide(x, getFunction(x, root.right));
                    case "^":
                        return Solve.exp(x, getFunction(x, root.right));
                }
            } else if (Objects.equals(root.right.data, "x")) {
                switch (root.data) {
                    case "+":
                        return Solve.add(getFunction(x, root.left), x);
                    case "-":
                        return Solve.subtract(getFunction(x, root.left), x);
                    case "*":
                        return Solve.multiply(getFunction(x, root.left), x);
                    case "/":
                        return Solve.divide(getFunction(x, root.left), x);
                    case "^":
                        return Solve.exp(getFunction(x, root.left), x);
                }
            } else {
                switch (root.data) {
                    case "+":
                        return Solve.add(getFunction(x, root.left), getFunction(x, root.right));
                    case "-":
                        return Solve.subtract(getFunction(x, root.left), getFunction(x, root.right));
                    case "*":
                        return Solve.multiply(getFunction(x, root.left), getFunction(x, root.right));
                    case "/":
                        return Solve.divide(getFunction(x, root.left), getFunction(x, root.right));
                    case "^":
                        return Solve.exp(getFunction(x, root.left), getFunction(x, root.right));
                }
            }
        }

        return -1;
    }

    public static void main(String[] args)
    {
        String s = "3";
        s = "(" + s;
        s += ")";
        nodePointer root = build(s);

        postorder(root);
        System.out.println();

        System.out.println(getFunction(5, root));
    }
}