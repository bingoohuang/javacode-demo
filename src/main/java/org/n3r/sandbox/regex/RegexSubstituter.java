package org.n3r.sandbox.regex;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>This is a utility class implementing some common regular
 * expression-based operations, using the <tt>java.util.regex</tt> classes.
 * The various operations are briefly described here; see the individual
 * methods for full details.</p>
 * <p/>
 * <h3>Substitution</h3>
 * <p/>
 * <p>The {@link #substitute} method implements Perl-like regular expression
 * substitution. It takes an edit string representing the substitution,
 * and a string to be edited. It returns the possibly edited string. The
 * substitution syntax is similar to Perl:</p>
 * <p/>
 * <blockquote><pre>s/regex/replacement/[g][i][m][o][x]</pre></blockquote>
 * <p/>
 * <p>The regular expressions compiled once, and the compiled versions are
 * cached in an internal LRU buffer. The buffer's size is fixed at the time
 * of instantiation.</p>
 * <p/>
 * <p>See the documentation for the {@link #substitute} method for full
 * details.</p>
 */
public class RegexSubstituter {
    private final String replacement;
    private final Pattern pattern;

    private boolean replaceAll = false; // global or once-only

    public RegexSubstituter(String substitutionCommand) {
        // Minimum size: 5 (s/a//)

        char delim = validateFirstTwoChars(substitutionCommand);

        String[] fields = Iterables.toArray(Splitter.on(delim).split(substitutionCommand), String.class);
        if (fields.length != 3 && fields.length != 4)
            throw new RuntimeException("Substitution command " + substitutionCommand
                    + " is a syntactically incorrect.");

        assert ((fields[0].length() == 1) && (fields[0].charAt(0) == 's'));

        String regex = fields[1];
        replacement = fields[2];

        int flags = 0;          // java.util.regex.Pattern.compile() flags
        if (fields.length == 4)
            flags = parseSubstitutionFlags(substitutionCommand, fields[3]);

        pattern = Pattern.compile(regex, flags);
    }

    public static char validateFirstTwoChars(String substitutionCommand) {
        if (substitutionCommand.length() < 5)
            throw new RuntimeException("Substitution command " + substitutionCommand
                    + " is too short.");

        if (substitutionCommand.charAt(0) != 's')
            throw new RuntimeException("Substitution command " + substitutionCommand
                    + " is a syntactically incorrect.");

        char delim = substitutionCommand.charAt(1);

        if (Character.isWhitespace(delim) || (Character.isLetter(delim)))
            throw new RuntimeException("Substitution command " + substitutionCommand +
                    " uses alphabetic or white-space delimiter " + delim);
        return delim;
    }

    public String substitute(String s) {
        Matcher matcher = pattern.matcher(s);

        // Note that Matcher.replaceFirst() and Matcher.replaceAll() handle
        // group replacement tokens $1, $2, etc.
        return replaceAll ? matcher.replaceAll(replacement)
                : matcher.replaceFirst(replacement);
    }

    /**
     * This method implements Perl-like regular expression substitution. It
     * takes an edit string representing the substitution, and a string to
     * be edited. It returns the possibly edited string. The substitution
     * syntax is similar to Perl:</p>
     * <p/>
     * <blockquote><pre>s/regex/replacement/[g][i][m][o][x]</pre></blockquote>
     * <p/>
     * <p>The regular expressions compiled once, and the compiled versions are
     * cached in an internal LRU buffer. The buffer's size is fixed at the time
     * of instantiation.</p>
     * <p/>
     * <p>Any non-alphabetic, printing character may be used in place of the
     * slashes. The modifiers generally have the same meanings as in Perl,
     * though some of them aren't actually supported (but are present solely
     * for syntactical compatibility).</p>
     * <p/>
     * <table border="0" cellpadding="2">
     * <tr valign="top">
     * <th>Modifier</th>
     * <th>Meaning</th>
     * </tr>
     * <p/>
     * <tr valign="top">
     * <td>g</td>
     * <td>Substitute for all occurrences of the regular expression.
     * not just the first one.</td>
     * </tr>
     * <p/>
     * <tr valign="top">
     * <td>i</td>
     * <td>Do case-insensitive pattern matching. This modifier corresponds
     * to the <tt>java.util.regex.Pattern.CASE_INSENSITIVE</tt> flag.
     * </td>
     * </tr>
     * <p/>
     * <tr valign="top">
     * <td>m</td>
     * <td>Treat the string is consisting of multiple lines. This modifier
     * corresponds to the <tt>java.util.regex.Pattern.MULTILINE</tt>
     * flag. It changes the meaning of "^" and "$" so that they
     * match just after or just before, respectively, a line
     * terminator or the end of the input sequence. By default
     * these expressions only match at the beginning and the end of
     * the entire input sequence.
     * </td>
     * </tr>
     * <p/>
     * <tr valign="top">
     * <td>o</td>
     * <td>Compile once. This modifier is ignored, since regular
     * expressions are always compiled once and stored in the
     * internal LRU buffer.</td>
     * </tr>
     * <p/>
     * <tr valign="top">
     * <td>u</td>
     * <td>Enables Unicode-aware case folding. This modifier corresponds
     * to the <tt>java.util.regex.UNICODE_CASE</tt> flag. When this
     * modifier is specified, case-insensitive matching, when
     * enabled by the CASE_INSENSITIVE flag, is done in a manner
     * consistent with the Unicode Standard. By default,
     * case-insensitive matching assumes that only characters in
     * the US-ASCII charset are being matched. Specifying this flag
     * may impose a performance penalty.
     * </td>
     * </tr>
     * <p/>
     * <tr valign="top">
     * <td>x</td>
     * <td>Permits whitespace and comments in a pattern. This modifier
     * corresponds to the <tt>java.util.regex.Pattern.COMMENTS</tt>
     * flag. When this mode is active, whitespace is ignored, and
     * embedded comments starting with # are ignored until the end
     * of a line.
     * </td>
     * </tr>
     * </table>
     *
     * @param s                   string to edit
     * @param substitutionCommand the "s///" substitution command
     * @return the possibly edited string
     */
    public static String substitute(String s, String substitutionCommand) {
        return new RegexSubstituter(substitutionCommand).substitute(s);
    }

    public static List<RegexSubstituter> parse(String substitutionCommands) {
        List<RegexSubstituter> regexSubstituters = Lists.newArrayList();
        String commands = substitutionCommands;

        while (true) {
            commands = commands.trim();
            char delim = validateFirstTwoChars(commands);
            int lastDelimPos = StringUtils.ordinalIndexOf(commands, "" + delim, 3);
            if (lastDelimPos < 0) {
                throw new RuntimeException("Substitution command " + commands
                        + " is a syntactically incorrect.");
            }

            int spacePos = findSpacePos(commands, lastDelimPos + 1);
            if (spacePos < 0) {
                regexSubstituters.add(new RegexSubstituter(commands));
                break;
            }

            regexSubstituters.add(new RegexSubstituter(commands.substring(0, spacePos)));
            commands = commands.substring(spacePos);
        }


        return regexSubstituters;
    }

    private static int findSpacePos(String str, int index) {
        for (int i = index, ii = str.length(); i < ii; ++i) {
            char ch = str.charAt(i);
            if (Character.isWhitespace(ch)) return i;
        }

        return -1;
    }


    private int parseSubstitutionFlags(String substitutionCommand, String sFlags) {
        int flags = 0;          // java.util.regex.Pattern.compile() flags
        char[] modifiers = sFlags.toCharArray();

        for (int i = 0; i < modifiers.length; i++) {
            char mod = modifiers[i];
            switch (mod) {
                case 'g':
                    replaceAll = true;
                    break;
                case 'i':
                    flags |= Pattern.CASE_INSENSITIVE;
                    break;
                case 'm':
                    flags |= Pattern.MULTILINE;
                    break;
                case 'o':
                    // N/A
                    break;
                case 'x':
                    flags |= Pattern.COMMENTS;
                    break;
                default:
                    throw new RuntimeException("Substitution command " + substitutionCommand
                            + " has unknown modifier  character \"" + mod + "\".");
            }
        }
        return flags;
    }
}