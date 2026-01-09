package org.farhan.dsl.asciidoc.parser.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalAsciiDocLexer extends Lexer {
    public static final int RULE_WORD=6;
    public static final int RULE_SL_COMMENT=8;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__11=11;
    public static final int T__33=33;
    public static final int T__12=12;
    public static final int T__34=34;
    public static final int T__13=13;
    public static final int T__14=14;
    public static final int EOF=-1;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__10=10;
    public static final int T__32=32;
    public static final int T__9=9;
    public static final int RULE_EOL=4;
    public static final int RULE_WS=7;
    public static final int RULE_RAWTEXT=5;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int T__29=29;
    public static final int T__22=22;
    public static final int T__23=23;
    public static final int T__24=24;
    public static final int T__25=25;
    public static final int T__20=20;
    public static final int T__21=21;

    // delegates
    // delegators

    public InternalAsciiDocLexer() {;} 
    public InternalAsciiDocLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public InternalAsciiDocLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "InternalAsciiDoc.g"; }

    // $ANTLR start "T__9"
    public final void mT__9() throws RecognitionException {
        try {
            int _type = T__9;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:11:6: ( '=' )
            // InternalAsciiDoc.g:11:8: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__9"

    // $ANTLR start "T__10"
    public final void mT__10() throws RecognitionException {
        try {
            int _type = T__10;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:12:7: ( 'Step-Object:' )
            // InternalAsciiDoc.g:12:9: 'Step-Object:'
            {
            match("Step-Object:"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__10"

    // $ANTLR start "T__11"
    public final void mT__11() throws RecognitionException {
        try {
            int _type = T__11;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:13:7: ( '==' )
            // InternalAsciiDoc.g:13:9: '=='
            {
            match("=="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__11"

    // $ANTLR start "T__12"
    public final void mT__12() throws RecognitionException {
        try {
            int _type = T__12;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:14:7: ( 'Step-Definition:' )
            // InternalAsciiDoc.g:14:9: 'Step-Definition:'
            {
            match("Step-Definition:"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__12"

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:15:7: ( '*' )
            // InternalAsciiDoc.g:15:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:16:7: ( 'Step-Parameters:' )
            // InternalAsciiDoc.g:16:9: 'Step-Parameters:'
            {
            match("Step-Parameters:"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:17:7: ( 'Test-Suite:' )
            // InternalAsciiDoc.g:17:9: 'Test-Suite:'
            {
            match("Test-Suite:"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:18:7: ( 'Test-Setup:' )
            // InternalAsciiDoc.g:18:9: 'Test-Setup:'
            {
            match("Test-Setup:"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:19:7: ( 'Test-Case:' )
            // InternalAsciiDoc.g:19:9: 'Test-Case:'
            {
            match("Test-Case:"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:20:7: ( 'Test-Data:' )
            // InternalAsciiDoc.g:20:9: 'Test-Data:'
            {
            match("Test-Data:"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:21:7: ( 'Given:' )
            // InternalAsciiDoc.g:21:9: 'Given:'
            {
            match("Given:"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:22:7: ( 'When:' )
            // InternalAsciiDoc.g:22:9: 'When:'
            {
            match("When:"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:23:7: ( 'Then:' )
            // InternalAsciiDoc.g:23:9: 'Then:'
            {
            match("Then:"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:24:7: ( 'And:' )
            // InternalAsciiDoc.g:24:9: 'And:'
            {
            match("And:"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:25:7: ( '+' )
            // InternalAsciiDoc.g:25:9: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "T__24"
    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:26:7: ( '|===' )
            // InternalAsciiDoc.g:26:9: '|==='
            {
            match("|==="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__24"

    // $ANTLR start "T__25"
    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:27:7: ( '|' )
            // InternalAsciiDoc.g:27:9: '|'
            {
            match('|'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__25"

    // $ANTLR start "T__26"
    public final void mT__26() throws RecognitionException {
        try {
            int _type = T__26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:28:7: ( 'file' )
            // InternalAsciiDoc.g:28:9: 'file'
            {
            match("file"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__26"

    // $ANTLR start "T__27"
    public final void mT__27() throws RecognitionException {
        try {
            int _type = T__27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:29:7: ( 'page' )
            // InternalAsciiDoc.g:29:9: 'page'
            {
            match("page"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__27"

    // $ANTLR start "T__28"
    public final void mT__28() throws RecognitionException {
        try {
            int _type = T__28;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:30:7: ( 'response' )
            // InternalAsciiDoc.g:30:9: 'response'
            {
            match("response"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__28"

    // $ANTLR start "T__29"
    public final void mT__29() throws RecognitionException {
        try {
            int _type = T__29;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:31:7: ( 'dialog' )
            // InternalAsciiDoc.g:31:9: 'dialog'
            {
            match("dialog"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__29"

    // $ANTLR start "T__30"
    public final void mT__30() throws RecognitionException {
        try {
            int _type = T__30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:32:7: ( 'directory' )
            // InternalAsciiDoc.g:32:9: 'directory'
            {
            match("directory"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__30"

    // $ANTLR start "T__31"
    public final void mT__31() throws RecognitionException {
        try {
            int _type = T__31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:33:7: ( 'request' )
            // InternalAsciiDoc.g:33:9: 'request'
            {
            match("request"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__31"

    // $ANTLR start "T__32"
    public final void mT__32() throws RecognitionException {
        try {
            int _type = T__32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:34:7: ( 'goal' )
            // InternalAsciiDoc.g:34:9: 'goal'
            {
            match("goal"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__32"

    // $ANTLR start "T__33"
    public final void mT__33() throws RecognitionException {
        try {
            int _type = T__33;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:35:7: ( 'job' )
            // InternalAsciiDoc.g:35:9: 'job'
            {
            match("job"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__33"

    // $ANTLR start "T__34"
    public final void mT__34() throws RecognitionException {
        try {
            int _type = T__34;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:36:7: ( 'action' )
            // InternalAsciiDoc.g:36:9: 'action'
            {
            match("action"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__34"

    // $ANTLR start "RULE_WS"
    public final void mRULE_WS() throws RecognitionException {
        try {
            int _type = RULE_WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:1682:9: ( ( ' ' | '\\t' | '\\r' )+ )
            // InternalAsciiDoc.g:1682:11: ( ' ' | '\\t' | '\\r' )+
            {
            // InternalAsciiDoc.g:1682:11: ( ' ' | '\\t' | '\\r' )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='\t'||LA1_0=='\r'||LA1_0==' ') ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalAsciiDoc.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)=='\r'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_WS"

    // $ANTLR start "RULE_SL_COMMENT"
    public final void mRULE_SL_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_SL_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:1684:17: ( '#' (~ ( '\\n' ) )* RULE_EOL )
            // InternalAsciiDoc.g:1684:19: '#' (~ ( '\\n' ) )* RULE_EOL
            {
            match('#'); 
            // InternalAsciiDoc.g:1684:23: (~ ( '\\n' ) )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='\u0000' && LA2_0<='\t')||(LA2_0>='\u000B' && LA2_0<='\uFFFF')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // InternalAsciiDoc.g:1684:23: ~ ( '\\n' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            mRULE_EOL(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_SL_COMMENT"

    // $ANTLR start "RULE_EOL"
    public final void mRULE_EOL() throws RecognitionException {
        try {
            int _type = RULE_EOL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:1686:10: ( ( '\\n' )+ )
            // InternalAsciiDoc.g:1686:12: ( '\\n' )+
            {
            // InternalAsciiDoc.g:1686:12: ( '\\n' )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0=='\n') ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // InternalAsciiDoc.g:1686:12: '\\n'
            	    {
            	    match('\n'); 

            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_EOL"

    // $ANTLR start "RULE_RAWTEXT"
    public final void mRULE_RAWTEXT() throws RecognitionException {
        try {
            int _type = RULE_RAWTEXT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:1688:14: ( '----' '\\n' ( . )+ '\\n' '----' )
            // InternalAsciiDoc.g:1688:16: '----' '\\n' ( . )+ '\\n' '----'
            {
            match("----"); 

            match('\n'); 
            // InternalAsciiDoc.g:1688:28: ( . )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0=='\n') ) {
                    int LA4_1 = input.LA(2);

                    if ( (LA4_1=='-') ) {
                        int LA4_3 = input.LA(3);

                        if ( (LA4_3=='-') ) {
                            int LA4_4 = input.LA(4);

                            if ( (LA4_4=='-') ) {
                                int LA4_5 = input.LA(5);

                                if ( (LA4_5=='-') ) {
                                    alt4=2;
                                }
                                else if ( ((LA4_5>='\u0000' && LA4_5<=',')||(LA4_5>='.' && LA4_5<='\uFFFF')) ) {
                                    alt4=1;
                                }


                            }
                            else if ( ((LA4_4>='\u0000' && LA4_4<=',')||(LA4_4>='.' && LA4_4<='\uFFFF')) ) {
                                alt4=1;
                            }


                        }
                        else if ( ((LA4_3>='\u0000' && LA4_3<=',')||(LA4_3>='.' && LA4_3<='\uFFFF')) ) {
                            alt4=1;
                        }


                    }
                    else if ( ((LA4_1>='\u0000' && LA4_1<=',')||(LA4_1>='.' && LA4_1<='\uFFFF')) ) {
                        alt4=1;
                    }


                }
                else if ( ((LA4_0>='\u0000' && LA4_0<='\t')||(LA4_0>='\u000B' && LA4_0<='\uFFFF')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // InternalAsciiDoc.g:1688:28: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);

            match('\n'); 
            match("----"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_RAWTEXT"

    // $ANTLR start "RULE_WORD"
    public final void mRULE_WORD() throws RecognitionException {
        try {
            int _type = RULE_WORD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalAsciiDoc.g:1690:11: ( (~ ( ( ' ' | '\\t' | '\\r' | '\\n' ) ) )+ )
            // InternalAsciiDoc.g:1690:13: (~ ( ( ' ' | '\\t' | '\\r' | '\\n' ) ) )+
            {
            // InternalAsciiDoc.g:1690:13: (~ ( ( ' ' | '\\t' | '\\r' | '\\n' ) ) )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='\u0000' && LA5_0<='\b')||(LA5_0>='\u000B' && LA5_0<='\f')||(LA5_0>='\u000E' && LA5_0<='\u001F')||(LA5_0>='!' && LA5_0<='\uFFFF')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // InternalAsciiDoc.g:1690:13: ~ ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\b')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\u001F')||(input.LA(1)>='!' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_WORD"

    public void mTokens() throws RecognitionException {
        // InternalAsciiDoc.g:1:8: ( T__9 | T__10 | T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | RULE_WS | RULE_SL_COMMENT | RULE_EOL | RULE_RAWTEXT | RULE_WORD )
        int alt6=31;
        alt6 = dfa6.predict(input);
        switch (alt6) {
            case 1 :
                // InternalAsciiDoc.g:1:10: T__9
                {
                mT__9(); 

                }
                break;
            case 2 :
                // InternalAsciiDoc.g:1:15: T__10
                {
                mT__10(); 

                }
                break;
            case 3 :
                // InternalAsciiDoc.g:1:21: T__11
                {
                mT__11(); 

                }
                break;
            case 4 :
                // InternalAsciiDoc.g:1:27: T__12
                {
                mT__12(); 

                }
                break;
            case 5 :
                // InternalAsciiDoc.g:1:33: T__13
                {
                mT__13(); 

                }
                break;
            case 6 :
                // InternalAsciiDoc.g:1:39: T__14
                {
                mT__14(); 

                }
                break;
            case 7 :
                // InternalAsciiDoc.g:1:45: T__15
                {
                mT__15(); 

                }
                break;
            case 8 :
                // InternalAsciiDoc.g:1:51: T__16
                {
                mT__16(); 

                }
                break;
            case 9 :
                // InternalAsciiDoc.g:1:57: T__17
                {
                mT__17(); 

                }
                break;
            case 10 :
                // InternalAsciiDoc.g:1:63: T__18
                {
                mT__18(); 

                }
                break;
            case 11 :
                // InternalAsciiDoc.g:1:69: T__19
                {
                mT__19(); 

                }
                break;
            case 12 :
                // InternalAsciiDoc.g:1:75: T__20
                {
                mT__20(); 

                }
                break;
            case 13 :
                // InternalAsciiDoc.g:1:81: T__21
                {
                mT__21(); 

                }
                break;
            case 14 :
                // InternalAsciiDoc.g:1:87: T__22
                {
                mT__22(); 

                }
                break;
            case 15 :
                // InternalAsciiDoc.g:1:93: T__23
                {
                mT__23(); 

                }
                break;
            case 16 :
                // InternalAsciiDoc.g:1:99: T__24
                {
                mT__24(); 

                }
                break;
            case 17 :
                // InternalAsciiDoc.g:1:105: T__25
                {
                mT__25(); 

                }
                break;
            case 18 :
                // InternalAsciiDoc.g:1:111: T__26
                {
                mT__26(); 

                }
                break;
            case 19 :
                // InternalAsciiDoc.g:1:117: T__27
                {
                mT__27(); 

                }
                break;
            case 20 :
                // InternalAsciiDoc.g:1:123: T__28
                {
                mT__28(); 

                }
                break;
            case 21 :
                // InternalAsciiDoc.g:1:129: T__29
                {
                mT__29(); 

                }
                break;
            case 22 :
                // InternalAsciiDoc.g:1:135: T__30
                {
                mT__30(); 

                }
                break;
            case 23 :
                // InternalAsciiDoc.g:1:141: T__31
                {
                mT__31(); 

                }
                break;
            case 24 :
                // InternalAsciiDoc.g:1:147: T__32
                {
                mT__32(); 

                }
                break;
            case 25 :
                // InternalAsciiDoc.g:1:153: T__33
                {
                mT__33(); 

                }
                break;
            case 26 :
                // InternalAsciiDoc.g:1:159: T__34
                {
                mT__34(); 

                }
                break;
            case 27 :
                // InternalAsciiDoc.g:1:165: RULE_WS
                {
                mRULE_WS(); 

                }
                break;
            case 28 :
                // InternalAsciiDoc.g:1:173: RULE_SL_COMMENT
                {
                mRULE_SL_COMMENT(); 

                }
                break;
            case 29 :
                // InternalAsciiDoc.g:1:189: RULE_EOL
                {
                mRULE_EOL(); 

                }
                break;
            case 30 :
                // InternalAsciiDoc.g:1:198: RULE_RAWTEXT
                {
                mRULE_RAWTEXT(); 

                }
                break;
            case 31 :
                // InternalAsciiDoc.g:1:211: RULE_WORD
                {
                mRULE_WORD(); 

                }
                break;

        }

    }


    protected DFA6 dfa6 = new DFA6(this);
    static final String DFA6_eotS =
        "\1\uffff\1\27\1\25\1\31\4\25\1\37\1\41\7\25\1\uffff\1\25\1\uffff\1\25\1\uffff\1\54\1\uffff\1\25\1\uffff\5\25\1\uffff\1\25\1\uffff\10\25\1\uffff\1\25\1\uffff\16\25\1\114\7\25\1\124\1\125\1\126\1\127\4\25\1\134\1\uffff\4\25\1\145\1\25\1\147\4\uffff\4\25\1\uffff\1\25\1\uffff\6\25\1\uffff\1\164\1\uffff\2\25\1\167\1\25\1\171\7\25\1\uffff\1\25\1\u0082\1\uffff\1\25\1\uffff\7\25\1\u008b\1\uffff\10\25\1\uffff\1\u0094\5\25\1\u009a\1\u009b\1\uffff\3\25\1\u009f\1\u00a0\2\uffff\1\u00a1\2\25\3\uffff\6\25\1\u00aa\1\u00ab\2\uffff";
    static final String DFA6_eofS =
        "\u00ac\uffff";
    static final String DFA6_minS =
        "\2\0\1\164\1\0\1\145\1\151\1\150\1\156\2\0\1\151\1\141\1\145\1\151\2\157\1\143\1\uffff\1\0\1\uffff\1\55\1\uffff\1\0\1\uffff\1\145\1\uffff\1\163\1\145\1\166\1\145\1\144\1\uffff\1\75\1\uffff\1\154\1\147\1\161\2\141\1\142\1\164\1\0\1\uffff\1\55\1\uffff\1\160\1\164\1\156\1\145\1\156\1\72\1\75\2\145\1\160\1\165\1\154\1\145\1\154\1\0\1\151\3\55\1\72\1\156\1\72\4\0\1\157\1\145\1\157\1\143\1\0\1\uffff\1\157\1\12\1\104\1\103\1\0\1\72\1\0\4\uffff\1\156\1\163\1\147\1\164\1\uffff\1\156\1\uffff\1\142\1\145\1\141\1\145\2\141\1\uffff\1\0\1\uffff\1\163\1\164\1\0\1\157\1\0\1\152\1\146\1\162\1\151\1\164\1\163\1\164\1\uffff\1\145\1\0\1\uffff\1\162\1\uffff\1\145\1\151\1\141\1\164\1\165\1\145\1\141\1\0\1\uffff\1\171\1\143\1\156\1\155\1\145\1\160\2\72\1\uffff\1\0\1\164\1\151\1\145\2\72\2\0\1\uffff\1\72\2\164\2\0\2\uffff\1\0\1\151\1\145\3\uffff\1\157\1\162\1\156\1\163\2\72\2\0\2\uffff";
    static final String DFA6_maxS =
        "\2\uffff\1\164\1\uffff\1\150\1\151\1\150\1\156\2\uffff\1\151\1\141\1\145\1\151\2\157\1\143\1\uffff\1\uffff\1\uffff\1\55\1\uffff\1\uffff\1\uffff\1\145\1\uffff\1\163\1\145\1\166\1\145\1\144\1\uffff\1\75\1\uffff\1\154\1\147\1\163\1\162\1\141\1\142\1\164\1\uffff\1\uffff\1\55\1\uffff\1\160\1\164\1\156\1\145\1\156\1\72\1\75\2\145\1\160\1\165\1\154\1\145\1\154\1\uffff\1\151\3\55\1\72\1\156\1\72\4\uffff\1\157\1\145\1\157\1\143\1\uffff\1\uffff\1\157\1\12\1\120\1\123\1\uffff\1\72\1\uffff\4\uffff\1\156\1\163\1\147\1\164\1\uffff\1\156\1\uffff\1\142\1\145\1\141\1\165\2\141\1\uffff\1\uffff\1\uffff\1\163\1\164\1\uffff\1\157\1\uffff\1\152\1\146\1\162\1\151\1\164\1\163\1\164\1\uffff\1\145\1\uffff\1\uffff\1\162\1\uffff\1\145\1\151\1\141\1\164\1\165\1\145\1\141\1\uffff\1\uffff\1\171\1\143\1\156\1\155\1\145\1\160\2\72\1\uffff\1\uffff\1\164\1\151\1\145\2\72\2\uffff\1\uffff\1\72\2\164\2\uffff\2\uffff\1\uffff\1\151\1\145\3\uffff\1\157\1\162\1\156\1\163\2\72\2\uffff\2\uffff";
    static final String DFA6_acceptS =
        "\21\uffff\1\33\1\uffff\1\35\1\uffff\1\37\1\uffff\1\1\1\uffff\1\5\5\uffff\1\17\1\uffff\1\21\10\uffff\1\34\1\uffff\1\3\37\uffff\1\31\7\uffff\1\16\1\20\1\22\1\23\4\uffff\1\30\1\uffff\1\36\6\uffff\1\15\1\uffff\1\14\14\uffff\1\13\2\uffff\1\25\1\uffff\1\32\10\uffff\1\27\10\uffff\1\24\10\uffff\1\26\5\uffff\1\11\1\12\3\uffff\1\7\1\10\1\2\10\uffff\1\4\1\6";
    static final String DFA6_specialS =
        "\1\31\1\17\1\uffff\1\15\4\uffff\1\5\1\4\10\uffff\1\32\3\uffff\1\10\22\uffff\1\11\21\uffff\1\30\7\uffff\1\3\1\6\1\12\1\14\4\uffff\1\26\5\uffff\1\2\1\uffff\1\1\22\uffff\1\0\3\uffff\1\20\1\uffff\1\33\11\uffff\1\24\12\uffff\1\16\12\uffff\1\22\5\uffff\1\27\1\34\4\uffff\1\23\1\25\2\uffff\1\7\13\uffff\1\13\1\21\2\uffff}>";
    static final String[] DFA6_transitionS = {
            "\11\25\1\21\1\23\2\25\1\21\22\25\1\21\2\25\1\22\6\25\1\3\1\10\1\25\1\24\17\25\1\1\3\25\1\7\5\25\1\5\13\25\1\2\1\4\2\25\1\6\11\25\1\20\2\25\1\15\1\25\1\12\1\16\2\25\1\17\5\25\1\13\1\25\1\14\11\25\1\11\uff83\25",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\34\25\1\26\uffc2\25",
            "\1\30",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\1\32\2\uffff\1\33",
            "\1\34",
            "\1\35",
            "\1\36",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\34\25\1\40\uffc2\25",
            "\1\42",
            "\1\43",
            "\1\44",
            "\1\45",
            "\1\46",
            "\1\47",
            "\1\50",
            "",
            "\11\51\2\52\2\51\1\52\22\51\1\52\uffdf\51",
            "",
            "\1\53",
            "",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "",
            "\1\55",
            "",
            "\1\56",
            "\1\57",
            "\1\60",
            "\1\61",
            "\1\62",
            "",
            "\1\63",
            "",
            "\1\64",
            "\1\65",
            "\1\67\1\uffff\1\66",
            "\1\70\20\uffff\1\71",
            "\1\72",
            "\1\73",
            "\1\74",
            "\11\51\2\52\2\51\1\52\22\51\1\52\uffdf\51",
            "",
            "\1\75",
            "",
            "\1\76",
            "\1\77",
            "\1\100",
            "\1\101",
            "\1\102",
            "\1\103",
            "\1\104",
            "\1\105",
            "\1\106",
            "\1\107",
            "\1\110",
            "\1\111",
            "\1\112",
            "\1\113",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\1\115",
            "\1\116",
            "\1\117",
            "\1\120",
            "\1\121",
            "\1\122",
            "\1\123",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\1\130",
            "\1\131",
            "\1\132",
            "\1\133",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "",
            "\1\135",
            "\1\136",
            "\1\140\12\uffff\1\137\1\141",
            "\1\143\1\144\16\uffff\1\142",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\1\146",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "",
            "",
            "",
            "",
            "\1\150",
            "\1\151",
            "\1\152",
            "\1\153",
            "",
            "\1\154",
            "",
            "\1\155",
            "\1\156",
            "\1\157",
            "\1\161\17\uffff\1\160",
            "\1\162",
            "\1\163",
            "",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "",
            "\1\165",
            "\1\166",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\1\170",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\1\172",
            "\1\173",
            "\1\174",
            "\1\175",
            "\1\176",
            "\1\177",
            "\1\u0080",
            "",
            "\1\u0081",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "",
            "\1\u0083",
            "",
            "\1\u0084",
            "\1\u0085",
            "\1\u0086",
            "\1\u0087",
            "\1\u0088",
            "\1\u0089",
            "\1\u008a",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "",
            "\1\u008c",
            "\1\u008d",
            "\1\u008e",
            "\1\u008f",
            "\1\u0090",
            "\1\u0091",
            "\1\u0092",
            "\1\u0093",
            "",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\1\u0095",
            "\1\u0096",
            "\1\u0097",
            "\1\u0098",
            "\1\u0099",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "",
            "\1\u009c",
            "\1\u009d",
            "\1\u009e",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "",
            "",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\1\u00a2",
            "\1\u00a3",
            "",
            "",
            "",
            "\1\u00a4",
            "\1\u00a5",
            "\1\u00a6",
            "\1\u00a7",
            "\1\u00a8",
            "\1\u00a9",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "",
            ""
    };

    static final short[] DFA6_eot = DFA.unpackEncodedString(DFA6_eotS);
    static final short[] DFA6_eof = DFA.unpackEncodedString(DFA6_eofS);
    static final char[] DFA6_min = DFA.unpackEncodedStringToUnsignedChars(DFA6_minS);
    static final char[] DFA6_max = DFA.unpackEncodedStringToUnsignedChars(DFA6_maxS);
    static final short[] DFA6_accept = DFA.unpackEncodedString(DFA6_acceptS);
    static final short[] DFA6_special = DFA.unpackEncodedString(DFA6_specialS);
    static final short[][] DFA6_transition;

    static {
        int numStates = DFA6_transitionS.length;
        DFA6_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA6_transition[i] = DFA.unpackEncodedString(DFA6_transitionS[i]);
        }
    }

    class DFA6 extends DFA {

        public DFA6(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 6;
            this.eot = DFA6_eot;
            this.eof = DFA6_eof;
            this.min = DFA6_min;
            this.max = DFA6_max;
            this.accept = DFA6_accept;
            this.special = DFA6_special;
            this.transition = DFA6_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__9 | T__10 | T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | RULE_WS | RULE_SL_COMMENT | RULE_EOL | RULE_RAWTEXT | RULE_WORD );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA6_102 = input.LA(1);

                        s = -1;
                        if ( ((LA6_102>='\u0000' && LA6_102<='\b')||(LA6_102>='\u000B' && LA6_102<='\f')||(LA6_102>='\u000E' && LA6_102<='\u001F')||(LA6_102>='!' && LA6_102<='\uFFFF')) ) {s = 21;}

                        else s = 116;

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA6_83 = input.LA(1);

                        s = -1;
                        if ( ((LA6_83>='\u0000' && LA6_83<='\b')||(LA6_83>='\u000B' && LA6_83<='\f')||(LA6_83>='\u000E' && LA6_83<='\u001F')||(LA6_83>='!' && LA6_83<='\uFFFF')) ) {s = 21;}

                        else s = 103;

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA6_81 = input.LA(1);

                        s = -1;
                        if ( ((LA6_81>='\u0000' && LA6_81<='\b')||(LA6_81>='\u000B' && LA6_81<='\f')||(LA6_81>='\u000E' && LA6_81<='\u001F')||(LA6_81>='!' && LA6_81<='\uFFFF')) ) {s = 21;}

                        else s = 101;

                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA6_67 = input.LA(1);

                        s = -1;
                        if ( ((LA6_67>='\u0000' && LA6_67<='\b')||(LA6_67>='\u000B' && LA6_67<='\f')||(LA6_67>='\u000E' && LA6_67<='\u001F')||(LA6_67>='!' && LA6_67<='\uFFFF')) ) {s = 21;}

                        else s = 84;

                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA6_9 = input.LA(1);

                        s = -1;
                        if ( (LA6_9=='=') ) {s = 32;}

                        else if ( ((LA6_9>='\u0000' && LA6_9<='\b')||(LA6_9>='\u000B' && LA6_9<='\f')||(LA6_9>='\u000E' && LA6_9<='\u001F')||(LA6_9>='!' && LA6_9<='<')||(LA6_9>='>' && LA6_9<='\uFFFF')) ) {s = 21;}

                        else s = 33;

                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA6_8 = input.LA(1);

                        s = -1;
                        if ( ((LA6_8>='\u0000' && LA6_8<='\b')||(LA6_8>='\u000B' && LA6_8<='\f')||(LA6_8>='\u000E' && LA6_8<='\u001F')||(LA6_8>='!' && LA6_8<='\uFFFF')) ) {s = 21;}

                        else s = 31;

                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA6_68 = input.LA(1);

                        s = -1;
                        if ( ((LA6_68>='\u0000' && LA6_68<='\b')||(LA6_68>='\u000B' && LA6_68<='\f')||(LA6_68>='\u000E' && LA6_68<='\u001F')||(LA6_68>='!' && LA6_68<='\uFFFF')) ) {s = 21;}

                        else s = 85;

                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA6_156 = input.LA(1);

                        s = -1;
                        if ( ((LA6_156>='\u0000' && LA6_156<='\b')||(LA6_156>='\u000B' && LA6_156<='\f')||(LA6_156>='\u000E' && LA6_156<='\u001F')||(LA6_156>='!' && LA6_156<='\uFFFF')) ) {s = 21;}

                        else s = 161;

                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA6_22 = input.LA(1);

                        s = -1;
                        if ( ((LA6_22>='\u0000' && LA6_22<='\b')||(LA6_22>='\u000B' && LA6_22<='\f')||(LA6_22>='\u000E' && LA6_22<='\u001F')||(LA6_22>='!' && LA6_22<='\uFFFF')) ) {s = 21;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA6_41 = input.LA(1);

                        s = -1;
                        if ( ((LA6_41>='\t' && LA6_41<='\n')||LA6_41=='\r'||LA6_41==' ') ) {s = 42;}

                        else if ( ((LA6_41>='\u0000' && LA6_41<='\b')||(LA6_41>='\u000B' && LA6_41<='\f')||(LA6_41>='\u000E' && LA6_41<='\u001F')||(LA6_41>='!' && LA6_41<='\uFFFF')) ) {s = 41;}

                        else s = 21;

                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA6_69 = input.LA(1);

                        s = -1;
                        if ( ((LA6_69>='\u0000' && LA6_69<='\b')||(LA6_69>='\u000B' && LA6_69<='\f')||(LA6_69>='\u000E' && LA6_69<='\u001F')||(LA6_69>='!' && LA6_69<='\uFFFF')) ) {s = 21;}

                        else s = 86;

                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA6_168 = input.LA(1);

                        s = -1;
                        if ( ((LA6_168>='\u0000' && LA6_168<='\b')||(LA6_168>='\u000B' && LA6_168<='\f')||(LA6_168>='\u000E' && LA6_168<='\u001F')||(LA6_168>='!' && LA6_168<='\uFFFF')) ) {s = 21;}

                        else s = 170;

                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA6_70 = input.LA(1);

                        s = -1;
                        if ( ((LA6_70>='\u0000' && LA6_70<='\b')||(LA6_70>='\u000B' && LA6_70<='\f')||(LA6_70>='\u000E' && LA6_70<='\u001F')||(LA6_70>='!' && LA6_70<='\uFFFF')) ) {s = 21;}

                        else s = 87;

                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA6_3 = input.LA(1);

                        s = -1;
                        if ( ((LA6_3>='\u0000' && LA6_3<='\b')||(LA6_3>='\u000B' && LA6_3<='\f')||(LA6_3>='\u000E' && LA6_3<='\u001F')||(LA6_3>='!' && LA6_3<='\uFFFF')) ) {s = 21;}

                        else s = 25;

                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA6_129 = input.LA(1);

                        s = -1;
                        if ( ((LA6_129>='\u0000' && LA6_129<='\b')||(LA6_129>='\u000B' && LA6_129<='\f')||(LA6_129>='\u000E' && LA6_129<='\u001F')||(LA6_129>='!' && LA6_129<='\uFFFF')) ) {s = 21;}

                        else s = 139;

                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA6_1 = input.LA(1);

                        s = -1;
                        if ( (LA6_1=='=') ) {s = 22;}

                        else if ( ((LA6_1>='\u0000' && LA6_1<='\b')||(LA6_1>='\u000B' && LA6_1<='\f')||(LA6_1>='\u000E' && LA6_1<='\u001F')||(LA6_1>='!' && LA6_1<='<')||(LA6_1>='>' && LA6_1<='\uFFFF')) ) {s = 21;}

                        else s = 23;

                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA6_106 = input.LA(1);

                        s = -1;
                        if ( ((LA6_106>='\u0000' && LA6_106<='\b')||(LA6_106>='\u000B' && LA6_106<='\f')||(LA6_106>='\u000E' && LA6_106<='\u001F')||(LA6_106>='!' && LA6_106<='\uFFFF')) ) {s = 21;}

                        else s = 119;

                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA6_169 = input.LA(1);

                        s = -1;
                        if ( ((LA6_169>='\u0000' && LA6_169<='\b')||(LA6_169>='\u000B' && LA6_169<='\f')||(LA6_169>='\u000E' && LA6_169<='\u001F')||(LA6_169>='!' && LA6_169<='\uFFFF')) ) {s = 21;}

                        else s = 171;

                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA6_140 = input.LA(1);

                        s = -1;
                        if ( ((LA6_140>='\u0000' && LA6_140<='\b')||(LA6_140>='\u000B' && LA6_140<='\f')||(LA6_140>='\u000E' && LA6_140<='\u001F')||(LA6_140>='!' && LA6_140<='\uFFFF')) ) {s = 21;}

                        else s = 148;

                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA6_152 = input.LA(1);

                        s = -1;
                        if ( ((LA6_152>='\u0000' && LA6_152<='\b')||(LA6_152>='\u000B' && LA6_152<='\f')||(LA6_152>='\u000E' && LA6_152<='\u001F')||(LA6_152>='!' && LA6_152<='\uFFFF')) ) {s = 21;}

                        else s = 159;

                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA6_118 = input.LA(1);

                        s = -1;
                        if ( ((LA6_118>='\u0000' && LA6_118<='\b')||(LA6_118>='\u000B' && LA6_118<='\f')||(LA6_118>='\u000E' && LA6_118<='\u001F')||(LA6_118>='!' && LA6_118<='\uFFFF')) ) {s = 21;}

                        else s = 130;

                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA6_153 = input.LA(1);

                        s = -1;
                        if ( ((LA6_153>='\u0000' && LA6_153<='\b')||(LA6_153>='\u000B' && LA6_153<='\f')||(LA6_153>='\u000E' && LA6_153<='\u001F')||(LA6_153>='!' && LA6_153<='\uFFFF')) ) {s = 21;}

                        else s = 160;

                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA6_75 = input.LA(1);

                        s = -1;
                        if ( ((LA6_75>='\u0000' && LA6_75<='\b')||(LA6_75>='\u000B' && LA6_75<='\f')||(LA6_75>='\u000E' && LA6_75<='\u001F')||(LA6_75>='!' && LA6_75<='\uFFFF')) ) {s = 21;}

                        else s = 92;

                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA6_146 = input.LA(1);

                        s = -1;
                        if ( ((LA6_146>='\u0000' && LA6_146<='\b')||(LA6_146>='\u000B' && LA6_146<='\f')||(LA6_146>='\u000E' && LA6_146<='\u001F')||(LA6_146>='!' && LA6_146<='\uFFFF')) ) {s = 21;}

                        else s = 154;

                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA6_59 = input.LA(1);

                        s = -1;
                        if ( ((LA6_59>='\u0000' && LA6_59<='\b')||(LA6_59>='\u000B' && LA6_59<='\f')||(LA6_59>='\u000E' && LA6_59<='\u001F')||(LA6_59>='!' && LA6_59<='\uFFFF')) ) {s = 21;}

                        else s = 76;

                        if ( s>=0 ) return s;
                        break;
                    case 25 : 
                        int LA6_0 = input.LA(1);

                        s = -1;
                        if ( (LA6_0=='=') ) {s = 1;}

                        else if ( (LA6_0=='S') ) {s = 2;}

                        else if ( (LA6_0=='*') ) {s = 3;}

                        else if ( (LA6_0=='T') ) {s = 4;}

                        else if ( (LA6_0=='G') ) {s = 5;}

                        else if ( (LA6_0=='W') ) {s = 6;}

                        else if ( (LA6_0=='A') ) {s = 7;}

                        else if ( (LA6_0=='+') ) {s = 8;}

                        else if ( (LA6_0=='|') ) {s = 9;}

                        else if ( (LA6_0=='f') ) {s = 10;}

                        else if ( (LA6_0=='p') ) {s = 11;}

                        else if ( (LA6_0=='r') ) {s = 12;}

                        else if ( (LA6_0=='d') ) {s = 13;}

                        else if ( (LA6_0=='g') ) {s = 14;}

                        else if ( (LA6_0=='j') ) {s = 15;}

                        else if ( (LA6_0=='a') ) {s = 16;}

                        else if ( (LA6_0=='\t'||LA6_0=='\r'||LA6_0==' ') ) {s = 17;}

                        else if ( (LA6_0=='#') ) {s = 18;}

                        else if ( (LA6_0=='\n') ) {s = 19;}

                        else if ( (LA6_0=='-') ) {s = 20;}

                        else if ( ((LA6_0>='\u0000' && LA6_0<='\b')||(LA6_0>='\u000B' && LA6_0<='\f')||(LA6_0>='\u000E' && LA6_0<='\u001F')||(LA6_0>='!' && LA6_0<='\"')||(LA6_0>='$' && LA6_0<=')')||LA6_0==','||(LA6_0>='.' && LA6_0<='<')||(LA6_0>='>' && LA6_0<='@')||(LA6_0>='B' && LA6_0<='F')||(LA6_0>='H' && LA6_0<='R')||(LA6_0>='U' && LA6_0<='V')||(LA6_0>='X' && LA6_0<='`')||(LA6_0>='b' && LA6_0<='c')||LA6_0=='e'||(LA6_0>='h' && LA6_0<='i')||(LA6_0>='k' && LA6_0<='o')||LA6_0=='q'||(LA6_0>='s' && LA6_0<='{')||(LA6_0>='}' && LA6_0<='\uFFFF')) ) {s = 21;}

                        if ( s>=0 ) return s;
                        break;
                    case 26 : 
                        int LA6_18 = input.LA(1);

                        s = -1;
                        if ( ((LA6_18>='\u0000' && LA6_18<='\b')||(LA6_18>='\u000B' && LA6_18<='\f')||(LA6_18>='\u000E' && LA6_18<='\u001F')||(LA6_18>='!' && LA6_18<='\uFFFF')) ) {s = 41;}

                        else if ( ((LA6_18>='\t' && LA6_18<='\n')||LA6_18=='\r'||LA6_18==' ') ) {s = 42;}

                        else s = 21;

                        if ( s>=0 ) return s;
                        break;
                    case 27 : 
                        int LA6_108 = input.LA(1);

                        s = -1;
                        if ( ((LA6_108>='\u0000' && LA6_108<='\b')||(LA6_108>='\u000B' && LA6_108<='\f')||(LA6_108>='\u000E' && LA6_108<='\u001F')||(LA6_108>='!' && LA6_108<='\uFFFF')) ) {s = 21;}

                        else s = 121;

                        if ( s>=0 ) return s;
                        break;
                    case 28 : 
                        int LA6_147 = input.LA(1);

                        s = -1;
                        if ( ((LA6_147>='\u0000' && LA6_147<='\b')||(LA6_147>='\u000B' && LA6_147<='\f')||(LA6_147>='\u000E' && LA6_147<='\u001F')||(LA6_147>='!' && LA6_147<='\uFFFF')) ) {s = 21;}

                        else s = 155;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 6, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}