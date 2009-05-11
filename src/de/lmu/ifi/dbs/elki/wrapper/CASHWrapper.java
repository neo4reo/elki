package de.lmu.ifi.dbs.elki.wrapper;

import de.lmu.ifi.dbs.elki.algorithm.clustering.correlation.CASH;
import de.lmu.ifi.dbs.elki.data.DatabaseObject;
import de.lmu.ifi.dbs.elki.database.connection.FileBasedDatabaseConnection;
import de.lmu.ifi.dbs.elki.parser.ParameterizationFunctionLabelParser;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.IntParameter;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.OptionID;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.OptionUtil;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.UnusedParameterException;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.constraints.GreaterConstraint;

import java.util.List;

/**
 * Wrapper class for CASH algorithm.
 *
 * @author Elke Achtert
 * @param <O> object type
 */
public class CASHWrapper<O extends DatabaseObject> extends FileBasedDatabaseConnectionWrapper<O> {

    /**
     * Parameter to specify the threshold for minimum number of points in a cluster,
     * must be an integer greater than 0.
     * <p>Key: {@code -cash.minpts} </p>
     */
    private final IntParameter MINPTS_PARAM = new IntParameter(CASH.MINPTS_ID,
        new GreaterConstraint(0));

    /**
     * Parameter to specify the maximum level for splitting the hypercube,
     * must be an integer greater than 0.
     * <p>Key: {@code -cash.maxlevel} </p>
     */
    private final IntParameter MAXLEVEL_PARAM = new IntParameter(CASH.MAXLEVEL_ID,
        new GreaterConstraint(0));

    /**
     * Main method to run this wrapper.
     *
     * @param args the arguments to run this wrapper
     */
    public static void main(String[] args) {
        new CASHWrapper<DatabaseObject>().runCLIWrapper(args);
    }

    /**
     * Adds parameters
     * {@link #MINPTS_PARAM} and {@link #MAXLEVEL_PARAM}
     * to the option handler additionally to parameters of super class.
     */
    public CASHWrapper() {
        super();
        // parameter min points
        addOption(MINPTS_PARAM);

        // parameter max level
        addOption(MAXLEVEL_PARAM);
    }

    @Override
    public List<String> getKDDTaskParameters() throws UnusedParameterException {
        List<String> parameters = super.getKDDTaskParameters();

        // algorithm CASH
        OptionUtil.addParameter(parameters, OptionID.ALGORITHM, CASH.class.getName());

        // parser
        OptionUtil.addParameter(parameters, FileBasedDatabaseConnection.PARSER_ID, ParameterizationFunctionLabelParser.class.getName());

        // minpts
        OptionUtil.addParameter(parameters, MINPTS_PARAM, Integer.toString(MINPTS_PARAM.getValue()));
 
        // maxLevel
        OptionUtil.addParameter(parameters, MAXLEVEL_PARAM, Integer.toString(MAXLEVEL_PARAM.getValue()));

        return parameters;
    }
}
