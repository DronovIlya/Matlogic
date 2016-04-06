package ru.dronov.matlogic.parser;

import java.util.ArrayList;
import java.util.List;

public class ArithmeticTokenParser extends TokenParser {

    private final List<Integer> characters = new ArrayList<>();
    private final List<Integer> indexes = new ArrayList<>();
    private int lastIndex;

    public ArithmeticTokenParser(String input) {
        super(input);
    }

    @Override
    protected int nextChar() {
        int current = popLastIndex();
        // if we had previous values in stack
        if (current != -1) {
            return current;
        }

        current = super.nextChar();
        if (current != '(') {
            return current;
        }

        clear();
        while (!indexes.isEmpty()) {
            current = super.nextChar();
            switch (current) {
                case '(':
                    push(characters.size());
                    pushChar(current);
                    break;
                case ')':
                    current = super.nextChar();
                    if (current == '\'' || current == '*' || current == '+' || current == '=') {
                        pushChar(']');
                        update(popIndex(), '[');
                    } else {
                        popIndex();
                        pushChar(')');
                    }

                    if (current != -1) {
                        super.decChar();
                    }
                    break;
                default:
                    pushChar(current);
                    break;

            }
        }
        return nextChar();
    }

    private void clear() {
        indexes.clear();
        characters.clear();
        lastIndex = 0;
        pushChar('(');
        push(0);
    }

    private void push(int index) {
        indexes.add(index);
    }

    private void pushChar(int ch) {
        characters.add(ch);
    }

    private void update(int index, int ch) {
        characters.set(index, ch);
    }

    private int popLastIndex() {
        if (lastIndex >= characters.size()) {
            lastIndex = 0;
            characters.clear();
            return -1;
        }
        int result = characters.get(lastIndex);
        lastIndex++;
        return result;
    }

    private int popIndex() {
        int index = indexes.get(indexes.size() - 1);
        indexes.remove(indexes.size() - 1);
        return index;
    }

    @Override
    protected void decChar() {
        if (lastIndex > 0) {
            lastIndex--;
        } else {
            super.decChar();
        }
    }
}
