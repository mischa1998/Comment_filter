
interface TextAnalyzer {
    Label processText(String text);
}
enum Label {
    SPAM, NEGATIVE_TEXT, TOO_LONG, OK
}

abstract class KeywordAnalyzer implements TextAnalyzer {
	abstract protected String[] getKeyWords();
	abstract protected Label getLabel();
	public Label processText(String text) {
		String[] sword = this.getKeyWords();
		for (int i = 0; i < sword.length; i++) {
			if (text.contains(sword[i])) {
				return this.getLabel();
			}
		}
		return Label.OK;
	}
}

class NegativeTextAnalyzer extends KeywordAnalyzer {
	private String[] keywords = {":(", "=(", ":|"};
	public String[] getKeyWords() {
		return keywords;
	}
	public Label getLabel() {
		return Label.NEGATIVE_TEXT;
	}
}

class TooLongTextAnalyzer implements TextAnalyzer {
	private final int maxLength;
	public TooLongTextAnalyzer(int maxLength) {
		this.maxLength = maxLength;
	}
	public Label processText(String text) {
		if (text.length() <= maxLength) {
			return Label.OK;
		} else {
			return Label.TOO_LONG;
		}
	}
}

class SpamAnalyzer extends KeywordAnalyzer { //spamanalizer
	private String[] keyword;
	public SpamAnalyzer(String[] a) {
		keyword = new String[a.length];
		for (int i = 0; i < a.length; i++) {
			keyword[i] = a[i];
		}
	}
	public String[] getKeyWords() {
		return keyword;
	}
	public Label getLabel() {
		return Label.SPAM;
	}
}

public class Main {
	public Label checkLabels(TextAnalyzer[] analyzers, String text) {
		for (int i = 0; i < analyzers.length; i++) {
			Label a = analyzers[i].processText(text);
			if (a != Label.OK) {
				return a;
			}
		}
		return Label.OK;
	}


	public static void main(String[] args) {
		Main a = new Main();
		String[] tests = new String[8];
        tests[0] = "This comment is so good.";                            // OK
        tests[1] = "This comment is so Loooooooooooooooooooooooooooong."; // TOO_LONG
        tests[2] = "Very negative comment !!!!=(!!!!;";                   // NEGATIVE_TEXT
        tests[3] = "Very BAAAAAAAAAAAAAAAAAAAAAAAAD comment with :|;";    // NEGATIVE_TEXT or TOO_LONG
        tests[4] = "This comment is so bad....";                          // SPAM
        tests[5] = "The comment is a spam, maybeeeeeeeeeeeeeeeeeeeeee!";  // SPAM or TOO_LONG
        tests[6] = "Negative bad :( spam.";                               // SPAM or NEGATIVE_TEXT
        tests[7] = "Very bad, very neg =(, very ..................";      // SPAM or NEGATIVE_TEXT or TOO_LONG
        String[] spamKeywords = {"spam", "bad"};
        int commentMaxLength = 40;
        TextAnalyzer[] textAnalyzers1 = {
            new SpamAnalyzer(spamKeywords),
            new NegativeTextAnalyzer(),
            new TooLongTextAnalyzer(commentMaxLength)
        };
        TextAnalyzer[] textAnalyzers2 = {
            new SpamAnalyzer(spamKeywords),
            new TooLongTextAnalyzer(commentMaxLength),
            new NegativeTextAnalyzer()
        };
        TextAnalyzer[] textAnalyzers3 = {
            new TooLongTextAnalyzer(commentMaxLength),
            new SpamAnalyzer(spamKeywords),
            new NegativeTextAnalyzer()
        };
        TextAnalyzer[] textAnalyzers4 = {
            new TooLongTextAnalyzer(commentMaxLength),
            new NegativeTextAnalyzer(),
            new SpamAnalyzer(spamKeywords)
        };
        TextAnalyzer[] textAnalyzers5 = {
            new NegativeTextAnalyzer(),
            new SpamAnalyzer(spamKeywords),
            new TooLongTextAnalyzer(commentMaxLength)
        };
        TextAnalyzer[] textAnalyzers6 = {
            new NegativeTextAnalyzer(),
            new TooLongTextAnalyzer(commentMaxLength),
            new SpamAnalyzer(spamKeywords)
        };
        TextAnalyzer[][] textAnalyzers = {textAnalyzers1, textAnalyzers2, textAnalyzers3,
                                          textAnalyzers4, textAnalyzers5, textAnalyzers6};
        int numberOfAnalyzer; // номер анализатора, указанный в идентификаторе textAnalyzers{№}
        int numberOfTest = 0; // номер теста, который соответствует индексу тестовых комментариев
        for (String test : tests) {
            numberOfAnalyzer = 1;
            System.out.print("test #" + numberOfTest + ": ");
            System.out.println(test);
            for (TextAnalyzer[] analyzers : textAnalyzers) {
                System.out.print(numberOfAnalyzer + ": ");
                System.out.println(a.checkLabels(analyzers, test));
                numberOfAnalyzer++;
            }
            numberOfTest++;
        }
	}
}