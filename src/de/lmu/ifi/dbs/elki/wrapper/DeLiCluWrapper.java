package de.lmu.ifi.dbs.elki.wrapper;

import java.util.List;

import de.lmu.ifi.dbs.elki.algorithm.clustering.DeLiClu;
import de.lmu.ifi.dbs.elki.data.DatabaseObject;
import de.lmu.ifi.dbs.elki.database.SpatialIndexDatabase;
import de.lmu.ifi.dbs.elki.database.connection.AbstractDatabaseConnection;
import de.lmu.ifi.dbs.elki.index.tree.TreeIndex;
import de.lmu.ifi.dbs.elki.index.tree.spatial.SpatialIndex;
import de.lmu.ifi.dbs.elki.index.tree.spatial.rstarvariants.deliclu.DeLiCluTree;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.IntParameter;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.OptionID;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.OptionUtil;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.UnusedParameterException;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.constraints.GreaterConstraint;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.constraints.GreaterEqualConstraint;

/**
 * Wrapper class for the {@link DeLiClu} algorithm.
 *
 * @author Elke Achtert
 * @param <O> object type
 */
public class DeLiCluWrapper<O extends DatabaseObject> extends NormalizationWrapper<O> {
    /**
     * Parameter to specify the threshold for minimum number of points in
     * the epsilon-neighborhood of a point,
     * must be an integer greater than 0.
     * <p>Default value: {@code 10} </p>
     * <p>Key: {@code -deliclu.minpts} </p>
     */
    private final IntParameter MINPTS_PARAM = new IntParameter(
        DeLiClu.MINPTS_ID,
        new GreaterConstraint(0),
        10);

    /**
     * Parameter to specify the size of a page in bytes,
     * must be an integer greater than 0.
     * <p>Default value: {@code 4000} </p>
     * <p>Key: {@code -treeindex.pagesize} </p>
     */
    private final IntParameter PAGE_SIZE_PARAM = new IntParameter(
        TreeIndex.PAGE_SIZE_ID,
        new GreaterConstraint(0),
        4000);

    /**
     * Parameter to specify the size of the cache in bytes,
     * must be an integer equal to or greater than 0.
     * <p>Default value: {@link Integer#MAX_VALUE} </p>
     * <p>Key: {@code -treeindex.cachesize} </p>
     */
    private final IntParameter CACHE_SIZE_PARAM = new IntParameter(
        TreeIndex.CACHE_SIZE_ID,
        new GreaterEqualConstraint(0),
        Integer.MAX_VALUE);

    /**
     * Main method to run this wrapper.
     *
     * @param args the arguments to run this wrapper
     */
    public static void main(String[] args) {
        new DeLiCluWrapper<DatabaseObject>().runCLIWrapper(args);
    }

    /**
     * Adds parameters
     * {@link #MINPTS_PARAM}, {@link #PAGE_SIZE_PARAM}, and {@link #CACHE_SIZE_PARAM}
     * to the option handler additionally to parameters of super class.
     */
    public DeLiCluWrapper() {
        super();
        // parameter minpts
        addOption(MINPTS_PARAM);

        // parameter page size
        addOption(PAGE_SIZE_PARAM);

        // parameter cache size
        addOption(CACHE_SIZE_PARAM);
    }

    @Override
    public List<String> getKDDTaskParameters() throws UnusedParameterException {
        List<String> parameters = super.getKDDTaskParameters();

        // deliclu algorithm
        OptionUtil.addParameter(parameters, OptionID.ALGORITHM, DeLiClu.class.getName());

        // minpts
        OptionUtil.addParameter(parameters, MINPTS_PARAM, Integer.toString(MINPTS_PARAM.getValue()));

        // database
        OptionUtil.addParameter(parameters, AbstractDatabaseConnection.DATABASE_ID, SpatialIndexDatabase.class.getName());

        // index
        OptionUtil.addParameter(parameters, SpatialIndexDatabase.INDEX_ID, DeLiCluTree.class.getName());

        // bulk load
        OptionUtil.addFlag(parameters, SpatialIndex.BULK_LOAD_ID);

        // page size
        OptionUtil.addParameter(parameters, PAGE_SIZE_PARAM, Integer.toString(PAGE_SIZE_PARAM.getValue()));

        // cache size
        OptionUtil.addParameter(parameters, CACHE_SIZE_PARAM, Integer.toString(CACHE_SIZE_PARAM.getValue()));

        return parameters;
    }
}
