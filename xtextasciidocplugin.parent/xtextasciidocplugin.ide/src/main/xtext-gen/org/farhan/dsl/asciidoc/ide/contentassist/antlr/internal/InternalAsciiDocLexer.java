package org.farhan.dsl.asciidoc.ide.contentassist.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.Lexer;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalAsciiDocLexer extends Lexer {
    public static final int RULE_WORD=4;
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
    public static final int RULE_EOL=5;
    public static final int RULE_WS=7;
    public static final int RULE_RAWTEXT=6;
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
            // InternalAsciiDoc.g:11:6: ( 'file' )
            // InternalAsciiDoc.g:11:8: 'file'
            {
            match("file"); 


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
            // InternalAsciiDoc.g:12:7: ( 'page' )
            // InternalAsciiDoc.g:12:9: 'page'
            {
            match("page"); 


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
            // InternalAsciiDoc.g:13:7: ( 'response' )
            // InternalAsciiDoc.g:13:9: 'response'
            {
            match("response"); 


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
            // InternalAsciiDoc.g:14:7: ( 'dialog' )
            // InternalAsciiDoc.g:14:9: 'dialog'
            {
            match("dialog"); 


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
            // InternalAsciiDoc.g:15:7: ( 'directory' )
            // InternalAsciiDoc.g:15:9: 'directory'
            {
            match("directory"); 


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
            // InternalAsciiDoc.g:16:7: ( 'request' )
            // InternalAsciiDoc.g:16:9: 'request'
            {
            match("request"); 


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
            // InternalAsciiDoc.g:17:7: ( 'goal' )
            // InternalAsciiDoc.g:17:9: 'goal'
            {
            match("goal"); 


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
            // InternalAsciiDoc.g:18:7: ( 'job' )
            // InternalAsciiDoc.g:18:9: 'job'
            {
            match("job"); 


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
            // InternalAsciiDoc.g:19:7: ( 'action' )
            // InternalAsciiDoc.g:19:9: 'action'
            {
            match("action"); 


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
            // InternalAsciiDoc.g:20:7: ( '=' )
            // InternalAsciiDoc.g:20:9: '='
            {
            match('='); 

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
            // InternalAsciiDoc.g:21:7: ( 'Step-Object:' )
            // InternalAsciiDoc.g:21:9: 'Step-Object:'
            {
            match("Step-Object:"); 


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
            // InternalAsciiDoc.g:22:7: ( '==' )
            // InternalAsciiDoc.g:22:9: '=='
            {
            match("=="); 


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
            // InternalAsciiDoc.g:23:7: ( 'Step-Definition:' )
            // InternalAsciiDoc.g:23:9: 'Step-Definition:'
            {
            match("Step-Definition:"); 


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
            // InternalAsciiDoc.g:24:7: ( '*' )
            // InternalAsciiDoc.g:24:9: '*'
            {
            match('*'); 

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
            // InternalAsciiDoc.g:25:7: ( 'Step-Parameters:' )
            // InternalAsciiDoc.g:25:9: 'Step-Parameters:'
            {
            match("Step-Parameters:"); 


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
            // InternalAsciiDoc.g:26:7: ( 'Test-Suite:' )
            // InternalAsciiDoc.g:26:9: 'Test-Suite:'
            {
            match("Test-Suite:"); 


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
            // InternalAsciiDoc.g:27:7: ( 'Test-Setup:' )
            // InternalAsciiDoc.g:27:9: 'Test-Setup:'
            {
            match("Test-Setup:"); 


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
            // InternalAsciiDoc.g:28:7: ( 'Test-Case:' )
            // InternalAsciiDoc.g:28:9: 'Test-Case:'
            {
            match("Test-Case:"); 


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
            // InternalAsciiDoc.g:29:7: ( 'Test-Data:' )
            // InternalAsciiDoc.g:29:9: 'Test-Data:'
            {
            match("Test-Data:"); 


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
            // InternalAsciiDoc.g:30:7: ( 'Given:' )
            // InternalAsciiDoc.g:30:9: 'Given:'
            {
            match("Given:"); 


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
            // InternalAsciiDoc.g:31:7: ( 'When:' )
            // InternalAsciiDoc.g:31:9: 'When:'
            {
            match("When:"); 


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
            // InternalAsciiDoc.g:32:7: ( 'Then:' )
            // InternalAsciiDoc.g:32:9: 'Then:'
            {
            match("Then:"); 


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
            // InternalAsciiDoc.g:33:7: ( 'And:' )
            // InternalAsciiDoc.g:33:9: 'And:'
            {
            match("And:"); 


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
            // InternalAsciiDoc.g:34:7: ( '+' )
            // InternalAsciiDoc.g:34:9: '+'
            {
            match('+'); 

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
            // InternalAsciiDoc.g:35:7: ( '|===' )
            // InternalAsciiDoc.g:35:9: '|==='
            {
            match("|==="); 


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
            // InternalAsciiDoc.g:36:7: ( '|' )
            // InternalAsciiDoc.g:36:9: '|'
            {
            match('|'); 

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
            // InternalAsciiDoc.g:3954:9: ( ( ' ' | '\\t' | '\\r' )+ )
            // InternalAsciiDoc.g:3954:11: ( ' ' | '\\t' | '\\r' )+
            {
            // InternalAsciiDoc.g:3954:11: ( ' ' | '\\t' | '\\r' )+
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
            // InternalAsciiDoc.g:3956:17: ( '#' (~ ( '\\n' ) )* RULE_EOL )
            // InternalAsciiDoc.g:3956:19: '#' (~ ( '\\n' ) )* RULE_EOL
            {
            match('#'); 
            // InternalAsciiDoc.g:3956:23: (~ ( '\\n' ) )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='\u0000' && LA2_0<='\t')||(LA2_0>='\u000B' && LA2_0<='\uFFFF')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // InternalAsciiDoc.g:3956:23: ~ ( '\\n' )
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
            // InternalAsciiDoc.g:3958:10: ( ( '\\n' )+ )
            // InternalAsciiDoc.g:3958:12: ( '\\n' )+
            {
            // InternalAsciiDoc.g:3958:12: ( '\\n' )+
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
            	    // InternalAsciiDoc.g:3958:12: '\\n'
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
            // InternalAsciiDoc.g:3960:14: ( '----' '\\n' ( . )+ '\\n' '----' )
            // InternalAsciiDoc.g:3960:16: '----' '\\n' ( . )+ '\\n' '----'
            {
            match("----"); 

            match('\n'); 
            // InternalAsciiDoc.g:3960:28: ( . )+
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
            	    // InternalAsciiDoc.g:3960:28: .
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
            // InternalAsciiDoc.g:3962:11: ( (~ ( ( ' ' | '\\t' | '\\r' | '\\n' ) ) )+ )
            // InternalAsciiDoc.g:3962:13: (~ ( ( ' ' | '\\t' | '\\r' | '\\n' ) ) )+
            {
            // InternalAsciiDoc.g:3962:13: (~ ( ( ' ' | '\\t' | '\\r' | '\\n' ) ) )+
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
            	    // InternalAsciiDoc.g:3962:13: ~ ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
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
        "\1\uffff\7\25\1\36\1\25\1\40\4\25\1\46\1\50\1\uffff\1\25\1\uffff\1\25\1\uffff\7\25\1\65\1\uffff\1\25\1\uffff\5\25\1\uffff\1\25\1\uffff\1\25\1\uffff\10\25\1\105\1\25\1\uffff\10\25\1\117\1\120\4\25\1\125\1\uffff\6\25\1\134\1\135\1\25\2\uffff\4\25\1\uffff\3\25\1\152\1\25\1\154\3\uffff\2\25\1\157\1\25\1\161\6\25\1\uffff\1\171\1\uffff\1\25\1\173\1\uffff\1\25\1\uffff\7\25\1\uffff\1\u0084\1\uffff\10\25\1\uffff\1\u008d\7\25\1\uffff\5\25\1\u009a\1\u009b\3\25\1\u009f\1\u00a0\2\uffff\1\u00a1\2\25\3\uffff\6\25\1\u00aa\1\u00ab\2\uffff";
    static final String DFA6_eofS =
        "\u00ac\uffff";
    static final String DFA6_minS =
        "\1\0\1\151\1\141\1\145\1\151\2\157\1\143\1\0\1\164\1\0\1\145\1\151\1\150\1\156\2\0\1\uffff\1\0\1\uffff\1\55\1\uffff\1\154\1\147\1\161\2\141\1\142\1\164\1\0\1\uffff\1\145\1\uffff\1\163\1\145\1\166\1\145\1\144\1\uffff\1\75\1\uffff\1\0\1\uffff\1\55\2\145\1\160\1\165\1\154\1\145\1\154\1\0\1\151\1\uffff\1\160\1\164\1\156\1\145\1\156\1\72\1\75\1\55\2\0\1\157\1\145\1\157\1\143\1\0\1\uffff\1\157\2\55\1\72\1\156\1\72\2\0\1\12\2\uffff\1\156\1\163\1\147\1\164\1\uffff\1\156\1\104\1\103\1\0\1\72\1\0\3\uffff\1\163\1\164\1\0\1\157\1\0\1\142\1\145\1\141\1\145\2\141\1\uffff\1\0\1\uffff\1\145\1\0\1\uffff\1\162\1\uffff\1\152\1\146\1\162\1\151\1\164\1\163\1\164\1\uffff\1\0\1\uffff\1\171\1\145\1\151\1\141\1\164\1\165\1\145\1\141\1\uffff\1\0\1\143\1\156\1\155\1\145\1\160\2\72\1\uffff\1\164\1\151\1\145\2\72\2\0\1\72\2\164\2\0\2\uffff\1\0\1\151\1\145\3\uffff\1\157\1\162\1\156\1\163\2\72\2\0\2\uffff";
    static final String DFA6_maxS =
        "\1\uffff\1\151\1\141\1\145\1\151\2\157\1\143\1\uffff\1\164\1\uffff\1\150\1\151\1\150\1\156\2\uffff\1\uffff\1\uffff\1\uffff\1\55\1\uffff\1\154\1\147\1\163\1\162\1\141\1\142\1\164\1\uffff\1\uffff\1\145\1\uffff\1\163\1\145\1\166\1\145\1\144\1\uffff\1\75\1\uffff\1\uffff\1\uffff\1\55\2\145\1\160\1\165\1\154\1\145\1\154\1\uffff\1\151\1\uffff\1\160\1\164\1\156\1\145\1\156\1\72\1\75\1\55\2\uffff\1\157\1\145\1\157\1\143\1\uffff\1\uffff\1\157\2\55\1\72\1\156\1\72\2\uffff\1\12\2\uffff\1\156\1\163\1\147\1\164\1\uffff\1\156\1\120\1\123\1\uffff\1\72\1\uffff\3\uffff\1\163\1\164\1\uffff\1\157\1\uffff\1\142\1\145\1\141\1\165\2\141\1\uffff\1\uffff\1\uffff\1\145\1\uffff\1\uffff\1\162\1\uffff\1\152\1\146\1\162\1\151\1\164\1\163\1\164\1\uffff\1\uffff\1\uffff\1\171\1\145\1\151\1\141\1\164\1\165\1\145\1\141\1\uffff\1\uffff\1\143\1\156\1\155\1\145\1\160\2\72\1\uffff\1\164\1\151\1\145\2\72\2\uffff\1\72\2\164\2\uffff\2\uffff\1\uffff\1\151\1\145\3\uffff\1\157\1\162\1\156\1\163\2\72\2\uffff\2\uffff";
    static final String DFA6_acceptS =
        "\21\uffff\1\33\1\uffff\1\35\1\uffff\1\37\10\uffff\1\12\1\uffff\1\16\5\uffff\1\30\1\uffff\1\32\1\uffff\1\34\12\uffff\1\14\17\uffff\1\10\11\uffff\1\1\1\2\4\uffff\1\7\6\uffff\1\27\1\31\1\36\13\uffff\1\26\1\uffff\1\25\2\uffff\1\4\1\uffff\1\11\7\uffff\1\24\1\uffff\1\6\10\uffff\1\3\10\uffff\1\5\14\uffff\1\22\1\23\3\uffff\1\20\1\21\1\13\10\uffff\1\15\1\17";
    static final String DFA6_specialS =
        "\1\16\7\uffff\1\13\1\uffff\1\1\4\uffff\1\30\1\2\1\uffff\1\33\12\uffff\1\34\13\uffff\1\11\11\uffff\1\24\12\uffff\1\4\1\5\4\uffff\1\22\7\uffff\1\27\1\31\13\uffff\1\25\1\uffff\1\23\5\uffff\1\12\1\uffff\1\26\7\uffff\1\21\2\uffff\1\20\13\uffff\1\7\12\uffff\1\15\15\uffff\1\14\1\17\3\uffff\1\6\1\10\2\uffff\1\32\13\uffff\1\0\1\3\2\uffff}>";
    static final String[] DFA6_transitionS = {
            "\11\25\1\21\1\23\2\25\1\21\22\25\1\21\2\25\1\22\6\25\1\12\1\17\1\25\1\24\17\25\1\10\3\25\1\16\5\25\1\14\13\25\1\11\1\13\2\25\1\15\11\25\1\7\2\25\1\4\1\25\1\1\1\5\2\25\1\6\5\25\1\2\1\25\1\3\11\25\1\20\uff83\25",
            "\1\26",
            "\1\27",
            "\1\30",
            "\1\31",
            "\1\32",
            "\1\33",
            "\1\34",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\34\25\1\35\uffc2\25",
            "\1\37",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\1\41\2\uffff\1\42",
            "\1\43",
            "\1\44",
            "\1\45",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\34\25\1\47\uffc2\25",
            "",
            "\11\51\2\52\2\51\1\52\22\51\1\52\uffdf\51",
            "",
            "\1\53",
            "",
            "\1\54",
            "\1\55",
            "\1\57\1\uffff\1\56",
            "\1\60\20\uffff\1\61",
            "\1\62",
            "\1\63",
            "\1\64",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "",
            "\1\66",
            "",
            "\1\67",
            "\1\70",
            "\1\71",
            "\1\72",
            "\1\73",
            "",
            "\1\74",
            "",
            "\11\51\2\52\2\51\1\52\22\51\1\52\uffdf\51",
            "",
            "\1\75",
            "\1\76",
            "\1\77",
            "\1\100",
            "\1\101",
            "\1\102",
            "\1\103",
            "\1\104",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\1\106",
            "",
            "\1\107",
            "\1\110",
            "\1\111",
            "\1\112",
            "\1\113",
            "\1\114",
            "\1\115",
            "\1\116",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\1\121",
            "\1\122",
            "\1\123",
            "\1\124",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "",
            "\1\126",
            "\1\127",
            "\1\130",
            "\1\131",
            "\1\132",
            "\1\133",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\1\136",
            "",
            "",
            "\1\137",
            "\1\140",
            "\1\141",
            "\1\142",
            "",
            "\1\143",
            "\1\145\12\uffff\1\144\1\146",
            "\1\150\1\151\16\uffff\1\147",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\1\153",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "",
            "",
            "",
            "\1\155",
            "\1\156",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\1\160",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\1\162",
            "\1\163",
            "\1\164",
            "\1\166\17\uffff\1\165",
            "\1\167",
            "\1\170",
            "",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "",
            "\1\172",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "",
            "\1\174",
            "",
            "\1\175",
            "\1\176",
            "\1\177",
            "\1\u0080",
            "\1\u0081",
            "\1\u0082",
            "\1\u0083",
            "",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "",
            "\1\u0085",
            "\1\u0086",
            "\1\u0087",
            "\1\u0088",
            "\1\u0089",
            "\1\u008a",
            "\1\u008b",
            "\1\u008c",
            "",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\1\u008e",
            "\1\u008f",
            "\1\u0090",
            "\1\u0091",
            "\1\u0092",
            "\1\u0093",
            "\1\u0094",
            "",
            "\1\u0095",
            "\1\u0096",
            "\1\u0097",
            "\1\u0098",
            "\1\u0099",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
            "\11\25\2\uffff\2\25\1\uffff\22\25\1\uffff\uffdf\25",
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
                        int LA6_168 = input.LA(1);

                        s = -1;
                        if ( ((LA6_168>='\u0000' && LA6_168<='\b')||(LA6_168>='\u000B' && LA6_168<='\f')||(LA6_168>='\u000E' && LA6_168<='\u001F')||(LA6_168>='!' && LA6_168<='\uFFFF')) ) {s = 21;}

                        else s = 170;

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA6_10 = input.LA(1);

                        s = -1;
                        if ( ((LA6_10>='\u0000' && LA6_10<='\b')||(LA6_10>='\u000B' && LA6_10<='\f')||(LA6_10>='\u000E' && LA6_10<='\u001F')||(LA6_10>='!' && LA6_10<='\uFFFF')) ) {s = 21;}

                        else s = 32;

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA6_16 = input.LA(1);

                        s = -1;
                        if ( (LA6_16=='=') ) {s = 39;}

                        else if ( ((LA6_16>='\u0000' && LA6_16<='\b')||(LA6_16>='\u000B' && LA6_16<='\f')||(LA6_16>='\u000E' && LA6_16<='\u001F')||(LA6_16>='!' && LA6_16<='<')||(LA6_16>='>' && LA6_16<='\uFFFF')) ) {s = 21;}

                        else s = 40;

                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA6_169 = input.LA(1);

                        s = -1;
                        if ( ((LA6_169>='\u0000' && LA6_169<='\b')||(LA6_169>='\u000B' && LA6_169<='\f')||(LA6_169>='\u000E' && LA6_169<='\u001F')||(LA6_169>='!' && LA6_169<='\uFFFF')) ) {s = 21;}

                        else s = 171;

                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA6_62 = input.LA(1);

                        s = -1;
                        if ( ((LA6_62>='\u0000' && LA6_62<='\b')||(LA6_62>='\u000B' && LA6_62<='\f')||(LA6_62>='\u000E' && LA6_62<='\u001F')||(LA6_62>='!' && LA6_62<='\uFFFF')) ) {s = 21;}

                        else s = 79;

                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA6_63 = input.LA(1);

                        s = -1;
                        if ( ((LA6_63>='\u0000' && LA6_63<='\b')||(LA6_63>='\u000B' && LA6_63<='\f')||(LA6_63>='\u000E' && LA6_63<='\u001F')||(LA6_63>='!' && LA6_63<='\uFFFF')) ) {s = 21;}

                        else s = 80;

                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA6_152 = input.LA(1);

                        s = -1;
                        if ( ((LA6_152>='\u0000' && LA6_152<='\b')||(LA6_152>='\u000B' && LA6_152<='\f')||(LA6_152>='\u000E' && LA6_152<='\u001F')||(LA6_152>='!' && LA6_152<='\uFFFF')) ) {s = 21;}

                        else s = 159;

                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA6_122 = input.LA(1);

                        s = -1;
                        if ( ((LA6_122>='\u0000' && LA6_122<='\b')||(LA6_122>='\u000B' && LA6_122<='\f')||(LA6_122>='\u000E' && LA6_122<='\u001F')||(LA6_122>='!' && LA6_122<='\uFFFF')) ) {s = 21;}

                        else s = 132;

                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA6_153 = input.LA(1);

                        s = -1;
                        if ( ((LA6_153>='\u0000' && LA6_153<='\b')||(LA6_153>='\u000B' && LA6_153<='\f')||(LA6_153>='\u000E' && LA6_153<='\u001F')||(LA6_153>='!' && LA6_153<='\uFFFF')) ) {s = 21;}

                        else s = 160;

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
                        int LA6_97 = input.LA(1);

                        s = -1;
                        if ( ((LA6_97>='\u0000' && LA6_97<='\b')||(LA6_97>='\u000B' && LA6_97<='\f')||(LA6_97>='\u000E' && LA6_97<='\u001F')||(LA6_97>='!' && LA6_97<='\uFFFF')) ) {s = 21;}

                        else s = 111;

                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA6_8 = input.LA(1);

                        s = -1;
                        if ( (LA6_8=='=') ) {s = 29;}

                        else if ( ((LA6_8>='\u0000' && LA6_8<='\b')||(LA6_8>='\u000B' && LA6_8<='\f')||(LA6_8>='\u000E' && LA6_8<='\u001F')||(LA6_8>='!' && LA6_8<='<')||(LA6_8>='>' && LA6_8<='\uFFFF')) ) {s = 21;}

                        else s = 30;

                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA6_147 = input.LA(1);

                        s = -1;
                        if ( ((LA6_147>='\u0000' && LA6_147<='\b')||(LA6_147>='\u000B' && LA6_147<='\f')||(LA6_147>='\u000E' && LA6_147<='\u001F')||(LA6_147>='!' && LA6_147<='\uFFFF')) ) {s = 21;}

                        else s = 154;

                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA6_133 = input.LA(1);

                        s = -1;
                        if ( ((LA6_133>='\u0000' && LA6_133<='\b')||(LA6_133>='\u000B' && LA6_133<='\f')||(LA6_133>='\u000E' && LA6_133<='\u001F')||(LA6_133>='!' && LA6_133<='\uFFFF')) ) {s = 21;}

                        else s = 141;

                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA6_0 = input.LA(1);

                        s = -1;
                        if ( (LA6_0=='f') ) {s = 1;}

                        else if ( (LA6_0=='p') ) {s = 2;}

                        else if ( (LA6_0=='r') ) {s = 3;}

                        else if ( (LA6_0=='d') ) {s = 4;}

                        else if ( (LA6_0=='g') ) {s = 5;}

                        else if ( (LA6_0=='j') ) {s = 6;}

                        else if ( (LA6_0=='a') ) {s = 7;}

                        else if ( (LA6_0=='=') ) {s = 8;}

                        else if ( (LA6_0=='S') ) {s = 9;}

                        else if ( (LA6_0=='*') ) {s = 10;}

                        else if ( (LA6_0=='T') ) {s = 11;}

                        else if ( (LA6_0=='G') ) {s = 12;}

                        else if ( (LA6_0=='W') ) {s = 13;}

                        else if ( (LA6_0=='A') ) {s = 14;}

                        else if ( (LA6_0=='+') ) {s = 15;}

                        else if ( (LA6_0=='|') ) {s = 16;}

                        else if ( (LA6_0=='\t'||LA6_0=='\r'||LA6_0==' ') ) {s = 17;}

                        else if ( (LA6_0=='#') ) {s = 18;}

                        else if ( (LA6_0=='\n') ) {s = 19;}

                        else if ( (LA6_0=='-') ) {s = 20;}

                        else if ( ((LA6_0>='\u0000' && LA6_0<='\b')||(LA6_0>='\u000B' && LA6_0<='\f')||(LA6_0>='\u000E' && LA6_0<='\u001F')||(LA6_0>='!' && LA6_0<='\"')||(LA6_0>='$' && LA6_0<=')')||LA6_0==','||(LA6_0>='.' && LA6_0<='<')||(LA6_0>='>' && LA6_0<='@')||(LA6_0>='B' && LA6_0<='F')||(LA6_0>='H' && LA6_0<='R')||(LA6_0>='U' && LA6_0<='V')||(LA6_0>='X' && LA6_0<='`')||(LA6_0>='b' && LA6_0<='c')||LA6_0=='e'||(LA6_0>='h' && LA6_0<='i')||(LA6_0>='k' && LA6_0<='o')||LA6_0=='q'||(LA6_0>='s' && LA6_0<='{')||(LA6_0>='}' && LA6_0<='\uFFFF')) ) {s = 21;}

                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA6_148 = input.LA(1);

                        s = -1;
                        if ( ((LA6_148>='\u0000' && LA6_148<='\b')||(LA6_148>='\u000B' && LA6_148<='\f')||(LA6_148>='\u000E' && LA6_148<='\u001F')||(LA6_148>='!' && LA6_148<='\uFFFF')) ) {s = 21;}

                        else s = 155;

                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA6_110 = input.LA(1);

                        s = -1;
                        if ( ((LA6_110>='\u0000' && LA6_110<='\b')||(LA6_110>='\u000B' && LA6_110<='\f')||(LA6_110>='\u000E' && LA6_110<='\u001F')||(LA6_110>='!' && LA6_110<='\uFFFF')) ) {s = 21;}

                        else s = 123;

                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA6_107 = input.LA(1);

                        s = -1;
                        if ( ((LA6_107>='\u0000' && LA6_107<='\b')||(LA6_107>='\u000B' && LA6_107<='\f')||(LA6_107>='\u000E' && LA6_107<='\u001F')||(LA6_107>='!' && LA6_107<='\uFFFF')) ) {s = 21;}

                        else s = 121;

                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA6_68 = input.LA(1);

                        s = -1;
                        if ( ((LA6_68>='\u0000' && LA6_68<='\b')||(LA6_68>='\u000B' && LA6_68<='\f')||(LA6_68>='\u000E' && LA6_68<='\u001F')||(LA6_68>='!' && LA6_68<='\uFFFF')) ) {s = 21;}

                        else s = 85;

                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA6_91 = input.LA(1);

                        s = -1;
                        if ( ((LA6_91>='\u0000' && LA6_91<='\b')||(LA6_91>='\u000B' && LA6_91<='\f')||(LA6_91>='\u000E' && LA6_91<='\u001F')||(LA6_91>='!' && LA6_91<='\uFFFF')) ) {s = 21;}

                        else s = 108;

                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA6_51 = input.LA(1);

                        s = -1;
                        if ( ((LA6_51>='\u0000' && LA6_51<='\b')||(LA6_51>='\u000B' && LA6_51<='\f')||(LA6_51>='\u000E' && LA6_51<='\u001F')||(LA6_51>='!' && LA6_51<='\uFFFF')) ) {s = 21;}

                        else s = 69;

                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA6_89 = input.LA(1);

                        s = -1;
                        if ( ((LA6_89>='\u0000' && LA6_89<='\b')||(LA6_89>='\u000B' && LA6_89<='\f')||(LA6_89>='\u000E' && LA6_89<='\u001F')||(LA6_89>='!' && LA6_89<='\uFFFF')) ) {s = 21;}

                        else s = 106;

                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA6_99 = input.LA(1);

                        s = -1;
                        if ( ((LA6_99>='\u0000' && LA6_99<='\b')||(LA6_99>='\u000B' && LA6_99<='\f')||(LA6_99>='\u000E' && LA6_99<='\u001F')||(LA6_99>='!' && LA6_99<='\uFFFF')) ) {s = 21;}

                        else s = 113;

                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA6_76 = input.LA(1);

                        s = -1;
                        if ( ((LA6_76>='\u0000' && LA6_76<='\b')||(LA6_76>='\u000B' && LA6_76<='\f')||(LA6_76>='\u000E' && LA6_76<='\u001F')||(LA6_76>='!' && LA6_76<='\uFFFF')) ) {s = 21;}

                        else s = 92;

                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA6_15 = input.LA(1);

                        s = -1;
                        if ( ((LA6_15>='\u0000' && LA6_15<='\b')||(LA6_15>='\u000B' && LA6_15<='\f')||(LA6_15>='\u000E' && LA6_15<='\u001F')||(LA6_15>='!' && LA6_15<='\uFFFF')) ) {s = 21;}

                        else s = 38;

                        if ( s>=0 ) return s;
                        break;
                    case 25 : 
                        int LA6_77 = input.LA(1);

                        s = -1;
                        if ( ((LA6_77>='\u0000' && LA6_77<='\b')||(LA6_77>='\u000B' && LA6_77<='\f')||(LA6_77>='\u000E' && LA6_77<='\u001F')||(LA6_77>='!' && LA6_77<='\uFFFF')) ) {s = 21;}

                        else s = 93;

                        if ( s>=0 ) return s;
                        break;
                    case 26 : 
                        int LA6_156 = input.LA(1);

                        s = -1;
                        if ( ((LA6_156>='\u0000' && LA6_156<='\b')||(LA6_156>='\u000B' && LA6_156<='\f')||(LA6_156>='\u000E' && LA6_156<='\u001F')||(LA6_156>='!' && LA6_156<='\uFFFF')) ) {s = 21;}

                        else s = 161;

                        if ( s>=0 ) return s;
                        break;
                    case 27 : 
                        int LA6_18 = input.LA(1);

                        s = -1;
                        if ( ((LA6_18>='\u0000' && LA6_18<='\b')||(LA6_18>='\u000B' && LA6_18<='\f')||(LA6_18>='\u000E' && LA6_18<='\u001F')||(LA6_18>='!' && LA6_18<='\uFFFF')) ) {s = 41;}

                        else if ( ((LA6_18>='\t' && LA6_18<='\n')||LA6_18=='\r'||LA6_18==' ') ) {s = 42;}

                        else s = 21;

                        if ( s>=0 ) return s;
                        break;
                    case 28 : 
                        int LA6_29 = input.LA(1);

                        s = -1;
                        if ( ((LA6_29>='\u0000' && LA6_29<='\b')||(LA6_29>='\u000B' && LA6_29<='\f')||(LA6_29>='\u000E' && LA6_29<='\u001F')||(LA6_29>='!' && LA6_29<='\uFFFF')) ) {s = 21;}

                        else s = 53;

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