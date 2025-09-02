package org.farhan.dsl.asciidoc.ide.contentassist.antlr.internal;

import java.io.InputStream;
import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.DFA;
import org.farhan.dsl.asciidoc.services.AsciiDocGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalAsciiDocParser extends AbstractInternalContentAssistParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_INT", "RULE_STRING", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'Hello'", "'!'", "'from'"
    };
    public static final int RULE_ID=4;
    public static final int RULE_WS=9;
    public static final int RULE_STRING=6;
    public static final int RULE_ANY_OTHER=10;
    public static final int RULE_SL_COMMENT=8;
    public static final int RULE_INT=5;
    public static final int T__11=11;
    public static final int RULE_ML_COMMENT=7;
    public static final int T__12=12;
    public static final int T__13=13;
    public static final int EOF=-1;

    // delegates
    // delegators


        public InternalAsciiDocParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalAsciiDocParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalAsciiDocParser.tokenNames; }
    public String getGrammarFileName() { return "InternalAsciiDoc.g"; }


    	private AsciiDocGrammarAccess grammarAccess;

    	public void setGrammarAccess(AsciiDocGrammarAccess grammarAccess) {
    		this.grammarAccess = grammarAccess;
    	}

    	@Override
    	protected Grammar getGrammar() {
    		return grammarAccess.getGrammar();
    	}

    	@Override
    	protected String getValueForTokenName(String tokenName) {
    		return tokenName;
    	}



    // $ANTLR start "entryRuleModel"
    // InternalAsciiDoc.g:53:1: entryRuleModel : ruleModel EOF ;
    public final void entryRuleModel() throws RecognitionException {
        try {
            // InternalAsciiDoc.g:54:1: ( ruleModel EOF )
            // InternalAsciiDoc.g:55:1: ruleModel EOF
            {
             before(grammarAccess.getModelRule()); 
            pushFollow(FOLLOW_1);
            ruleModel();

            state._fsp--;

             after(grammarAccess.getModelRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleModel"


    // $ANTLR start "ruleModel"
    // InternalAsciiDoc.g:62:1: ruleModel : ( ( rule__Model__GreetingsAssignment )* ) ;
    public final void ruleModel() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalAsciiDoc.g:66:2: ( ( ( rule__Model__GreetingsAssignment )* ) )
            // InternalAsciiDoc.g:67:2: ( ( rule__Model__GreetingsAssignment )* )
            {
            // InternalAsciiDoc.g:67:2: ( ( rule__Model__GreetingsAssignment )* )
            // InternalAsciiDoc.g:68:3: ( rule__Model__GreetingsAssignment )*
            {
             before(grammarAccess.getModelAccess().getGreetingsAssignment()); 
            // InternalAsciiDoc.g:69:3: ( rule__Model__GreetingsAssignment )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==11) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalAsciiDoc.g:69:4: rule__Model__GreetingsAssignment
            	    {
            	    pushFollow(FOLLOW_3);
            	    rule__Model__GreetingsAssignment();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

             after(grammarAccess.getModelAccess().getGreetingsAssignment()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleModel"


    // $ANTLR start "entryRuleGreeting"
    // InternalAsciiDoc.g:78:1: entryRuleGreeting : ruleGreeting EOF ;
    public final void entryRuleGreeting() throws RecognitionException {
        try {
            // InternalAsciiDoc.g:79:1: ( ruleGreeting EOF )
            // InternalAsciiDoc.g:80:1: ruleGreeting EOF
            {
             before(grammarAccess.getGreetingRule()); 
            pushFollow(FOLLOW_1);
            ruleGreeting();

            state._fsp--;

             after(grammarAccess.getGreetingRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleGreeting"


    // $ANTLR start "ruleGreeting"
    // InternalAsciiDoc.g:87:1: ruleGreeting : ( ( rule__Greeting__Group__0 ) ) ;
    public final void ruleGreeting() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalAsciiDoc.g:91:2: ( ( ( rule__Greeting__Group__0 ) ) )
            // InternalAsciiDoc.g:92:2: ( ( rule__Greeting__Group__0 ) )
            {
            // InternalAsciiDoc.g:92:2: ( ( rule__Greeting__Group__0 ) )
            // InternalAsciiDoc.g:93:3: ( rule__Greeting__Group__0 )
            {
             before(grammarAccess.getGreetingAccess().getGroup()); 
            // InternalAsciiDoc.g:94:3: ( rule__Greeting__Group__0 )
            // InternalAsciiDoc.g:94:4: rule__Greeting__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Greeting__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getGreetingAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleGreeting"


    // $ANTLR start "rule__Greeting__Group__0"
    // InternalAsciiDoc.g:102:1: rule__Greeting__Group__0 : rule__Greeting__Group__0__Impl rule__Greeting__Group__1 ;
    public final void rule__Greeting__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalAsciiDoc.g:106:1: ( rule__Greeting__Group__0__Impl rule__Greeting__Group__1 )
            // InternalAsciiDoc.g:107:2: rule__Greeting__Group__0__Impl rule__Greeting__Group__1
            {
            pushFollow(FOLLOW_4);
            rule__Greeting__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Greeting__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Greeting__Group__0"


    // $ANTLR start "rule__Greeting__Group__0__Impl"
    // InternalAsciiDoc.g:114:1: rule__Greeting__Group__0__Impl : ( 'Hello' ) ;
    public final void rule__Greeting__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalAsciiDoc.g:118:1: ( ( 'Hello' ) )
            // InternalAsciiDoc.g:119:1: ( 'Hello' )
            {
            // InternalAsciiDoc.g:119:1: ( 'Hello' )
            // InternalAsciiDoc.g:120:2: 'Hello'
            {
             before(grammarAccess.getGreetingAccess().getHelloKeyword_0()); 
            match(input,11,FOLLOW_2); 
             after(grammarAccess.getGreetingAccess().getHelloKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Greeting__Group__0__Impl"


    // $ANTLR start "rule__Greeting__Group__1"
    // InternalAsciiDoc.g:129:1: rule__Greeting__Group__1 : rule__Greeting__Group__1__Impl rule__Greeting__Group__2 ;
    public final void rule__Greeting__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalAsciiDoc.g:133:1: ( rule__Greeting__Group__1__Impl rule__Greeting__Group__2 )
            // InternalAsciiDoc.g:134:2: rule__Greeting__Group__1__Impl rule__Greeting__Group__2
            {
            pushFollow(FOLLOW_5);
            rule__Greeting__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Greeting__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Greeting__Group__1"


    // $ANTLR start "rule__Greeting__Group__1__Impl"
    // InternalAsciiDoc.g:141:1: rule__Greeting__Group__1__Impl : ( ( rule__Greeting__NameAssignment_1 ) ) ;
    public final void rule__Greeting__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalAsciiDoc.g:145:1: ( ( ( rule__Greeting__NameAssignment_1 ) ) )
            // InternalAsciiDoc.g:146:1: ( ( rule__Greeting__NameAssignment_1 ) )
            {
            // InternalAsciiDoc.g:146:1: ( ( rule__Greeting__NameAssignment_1 ) )
            // InternalAsciiDoc.g:147:2: ( rule__Greeting__NameAssignment_1 )
            {
             before(grammarAccess.getGreetingAccess().getNameAssignment_1()); 
            // InternalAsciiDoc.g:148:2: ( rule__Greeting__NameAssignment_1 )
            // InternalAsciiDoc.g:148:3: rule__Greeting__NameAssignment_1
            {
            pushFollow(FOLLOW_2);
            rule__Greeting__NameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getGreetingAccess().getNameAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Greeting__Group__1__Impl"


    // $ANTLR start "rule__Greeting__Group__2"
    // InternalAsciiDoc.g:156:1: rule__Greeting__Group__2 : rule__Greeting__Group__2__Impl rule__Greeting__Group__3 ;
    public final void rule__Greeting__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalAsciiDoc.g:160:1: ( rule__Greeting__Group__2__Impl rule__Greeting__Group__3 )
            // InternalAsciiDoc.g:161:2: rule__Greeting__Group__2__Impl rule__Greeting__Group__3
            {
            pushFollow(FOLLOW_5);
            rule__Greeting__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Greeting__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Greeting__Group__2"


    // $ANTLR start "rule__Greeting__Group__2__Impl"
    // InternalAsciiDoc.g:168:1: rule__Greeting__Group__2__Impl : ( ( rule__Greeting__Group_2__0 )? ) ;
    public final void rule__Greeting__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalAsciiDoc.g:172:1: ( ( ( rule__Greeting__Group_2__0 )? ) )
            // InternalAsciiDoc.g:173:1: ( ( rule__Greeting__Group_2__0 )? )
            {
            // InternalAsciiDoc.g:173:1: ( ( rule__Greeting__Group_2__0 )? )
            // InternalAsciiDoc.g:174:2: ( rule__Greeting__Group_2__0 )?
            {
             before(grammarAccess.getGreetingAccess().getGroup_2()); 
            // InternalAsciiDoc.g:175:2: ( rule__Greeting__Group_2__0 )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==13) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // InternalAsciiDoc.g:175:3: rule__Greeting__Group_2__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Greeting__Group_2__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getGreetingAccess().getGroup_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Greeting__Group__2__Impl"


    // $ANTLR start "rule__Greeting__Group__3"
    // InternalAsciiDoc.g:183:1: rule__Greeting__Group__3 : rule__Greeting__Group__3__Impl ;
    public final void rule__Greeting__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalAsciiDoc.g:187:1: ( rule__Greeting__Group__3__Impl )
            // InternalAsciiDoc.g:188:2: rule__Greeting__Group__3__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Greeting__Group__3__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Greeting__Group__3"


    // $ANTLR start "rule__Greeting__Group__3__Impl"
    // InternalAsciiDoc.g:194:1: rule__Greeting__Group__3__Impl : ( '!' ) ;
    public final void rule__Greeting__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalAsciiDoc.g:198:1: ( ( '!' ) )
            // InternalAsciiDoc.g:199:1: ( '!' )
            {
            // InternalAsciiDoc.g:199:1: ( '!' )
            // InternalAsciiDoc.g:200:2: '!'
            {
             before(grammarAccess.getGreetingAccess().getExclamationMarkKeyword_3()); 
            match(input,12,FOLLOW_2); 
             after(grammarAccess.getGreetingAccess().getExclamationMarkKeyword_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Greeting__Group__3__Impl"


    // $ANTLR start "rule__Greeting__Group_2__0"
    // InternalAsciiDoc.g:210:1: rule__Greeting__Group_2__0 : rule__Greeting__Group_2__0__Impl rule__Greeting__Group_2__1 ;
    public final void rule__Greeting__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalAsciiDoc.g:214:1: ( rule__Greeting__Group_2__0__Impl rule__Greeting__Group_2__1 )
            // InternalAsciiDoc.g:215:2: rule__Greeting__Group_2__0__Impl rule__Greeting__Group_2__1
            {
            pushFollow(FOLLOW_4);
            rule__Greeting__Group_2__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Greeting__Group_2__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Greeting__Group_2__0"


    // $ANTLR start "rule__Greeting__Group_2__0__Impl"
    // InternalAsciiDoc.g:222:1: rule__Greeting__Group_2__0__Impl : ( 'from' ) ;
    public final void rule__Greeting__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalAsciiDoc.g:226:1: ( ( 'from' ) )
            // InternalAsciiDoc.g:227:1: ( 'from' )
            {
            // InternalAsciiDoc.g:227:1: ( 'from' )
            // InternalAsciiDoc.g:228:2: 'from'
            {
             before(grammarAccess.getGreetingAccess().getFromKeyword_2_0()); 
            match(input,13,FOLLOW_2); 
             after(grammarAccess.getGreetingAccess().getFromKeyword_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Greeting__Group_2__0__Impl"


    // $ANTLR start "rule__Greeting__Group_2__1"
    // InternalAsciiDoc.g:237:1: rule__Greeting__Group_2__1 : rule__Greeting__Group_2__1__Impl ;
    public final void rule__Greeting__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalAsciiDoc.g:241:1: ( rule__Greeting__Group_2__1__Impl )
            // InternalAsciiDoc.g:242:2: rule__Greeting__Group_2__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Greeting__Group_2__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Greeting__Group_2__1"


    // $ANTLR start "rule__Greeting__Group_2__1__Impl"
    // InternalAsciiDoc.g:248:1: rule__Greeting__Group_2__1__Impl : ( ( rule__Greeting__FromAssignment_2_1 ) ) ;
    public final void rule__Greeting__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalAsciiDoc.g:252:1: ( ( ( rule__Greeting__FromAssignment_2_1 ) ) )
            // InternalAsciiDoc.g:253:1: ( ( rule__Greeting__FromAssignment_2_1 ) )
            {
            // InternalAsciiDoc.g:253:1: ( ( rule__Greeting__FromAssignment_2_1 ) )
            // InternalAsciiDoc.g:254:2: ( rule__Greeting__FromAssignment_2_1 )
            {
             before(grammarAccess.getGreetingAccess().getFromAssignment_2_1()); 
            // InternalAsciiDoc.g:255:2: ( rule__Greeting__FromAssignment_2_1 )
            // InternalAsciiDoc.g:255:3: rule__Greeting__FromAssignment_2_1
            {
            pushFollow(FOLLOW_2);
            rule__Greeting__FromAssignment_2_1();

            state._fsp--;


            }

             after(grammarAccess.getGreetingAccess().getFromAssignment_2_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Greeting__Group_2__1__Impl"


    // $ANTLR start "rule__Model__GreetingsAssignment"
    // InternalAsciiDoc.g:264:1: rule__Model__GreetingsAssignment : ( ruleGreeting ) ;
    public final void rule__Model__GreetingsAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalAsciiDoc.g:268:1: ( ( ruleGreeting ) )
            // InternalAsciiDoc.g:269:2: ( ruleGreeting )
            {
            // InternalAsciiDoc.g:269:2: ( ruleGreeting )
            // InternalAsciiDoc.g:270:3: ruleGreeting
            {
             before(grammarAccess.getModelAccess().getGreetingsGreetingParserRuleCall_0()); 
            pushFollow(FOLLOW_2);
            ruleGreeting();

            state._fsp--;

             after(grammarAccess.getModelAccess().getGreetingsGreetingParserRuleCall_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Model__GreetingsAssignment"


    // $ANTLR start "rule__Greeting__NameAssignment_1"
    // InternalAsciiDoc.g:279:1: rule__Greeting__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__Greeting__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalAsciiDoc.g:283:1: ( ( RULE_ID ) )
            // InternalAsciiDoc.g:284:2: ( RULE_ID )
            {
            // InternalAsciiDoc.g:284:2: ( RULE_ID )
            // InternalAsciiDoc.g:285:3: RULE_ID
            {
             before(grammarAccess.getGreetingAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getGreetingAccess().getNameIDTerminalRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Greeting__NameAssignment_1"


    // $ANTLR start "rule__Greeting__FromAssignment_2_1"
    // InternalAsciiDoc.g:294:1: rule__Greeting__FromAssignment_2_1 : ( ( RULE_ID ) ) ;
    public final void rule__Greeting__FromAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalAsciiDoc.g:298:1: ( ( ( RULE_ID ) ) )
            // InternalAsciiDoc.g:299:2: ( ( RULE_ID ) )
            {
            // InternalAsciiDoc.g:299:2: ( ( RULE_ID ) )
            // InternalAsciiDoc.g:300:3: ( RULE_ID )
            {
             before(grammarAccess.getGreetingAccess().getFromGreetingCrossReference_2_1_0()); 
            // InternalAsciiDoc.g:301:3: ( RULE_ID )
            // InternalAsciiDoc.g:302:4: RULE_ID
            {
             before(grammarAccess.getGreetingAccess().getFromGreetingIDTerminalRuleCall_2_1_0_1()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getGreetingAccess().getFromGreetingIDTerminalRuleCall_2_1_0_1()); 

            }

             after(grammarAccess.getGreetingAccess().getFromGreetingCrossReference_2_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Greeting__FromAssignment_2_1"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000003000L});

}